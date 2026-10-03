package com.xulai.majruszlibrary.modhelper;

import com.xulai.majruszlibrary.platform.Side;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

class ClassFinder {
	final List< Class< ? > > classes = new ArrayList<>();
	final ModHelper helper;

	public ClassFinder( ModHelper helper ) {
		this.helper = helper;
	}

	public void findClasses() {
		this.addUnique( this.findClassesInPackage() );
		this.addUnique( this.findClassesInJar( "mods" ) );
		this.addUnique( this.findClassesInJar( "libs" ) );
		if( this.classes.isEmpty() ) {
			throw new IllegalStateException( "ClassFinder did not find any classes" );
		}
	}

	public < Type > Type getInstance( Predicate< Class< ? > > predicate ) {
		return ( Type )this.getInstances( predicate ).get( 0 );
	}

	public List< ? > getInstances( Predicate< Class< ? > > predicate ) {
		return this.classes.stream()
			.filter( predicate )
			.map( clazz->{
				try {
					return clazz.getConstructor().newInstance();
				} catch( Exception exception ) {
					return null;
				}
			} )
			.toList();
	}

	private String getModPackage() {
		return "com.xulai.%s".formatted( this.helper.getModId() );
	}

	private String getModPackagePath() {
		return this.getModPackage().replace( '.', '/' );
	}

	private List< Class< ? > > findClassesInPackage() {
		List< Class< ? > > classes = new ArrayList<>();
		URL resource = this.getClass().getClassLoader().getResource( this.getModPackagePath() );
		if( resource == null || !"file".equals( resource.getProtocol() ) ) {
			return classes;
		}

		try {
			Path packageDir = Paths.get( resource.toURI() );
			try( Stream< Path > paths = Files.walk( packageDir ) ) {
				paths.filter( p->p.getFileName().toString().endsWith( ".class" ) ).forEach( p->{
					String name = p.toString().substring( packageDir.toString().length() )
						.replace( File.separatorChar, '.' )
						.replace( '/', '.' )
						.replace( ".class", "" );
					Class< ? > clazz;
					try {
						clazz = this.tryToLoad( "%s.%s".formatted( this.getModPackage(), name ) );
					} catch( Exception exception ) {
						this.helper.logError( "Failed to find class: %s", exception.toString() );
						return;
					}

					if( clazz != null ) {
						classes.add( clazz );
					}
				} );
			}
		} catch( Exception exception ) {
			this.helper.logError( "Failed to scan own package: %s", exception.toString() );
		}

		return classes;
	}

	private List< Class< ? > > findClassesInJar( String directory ) {
		List< Class< ? > > classes = new ArrayList<>();
		File mods = Paths.get( "./%s".formatted( directory ) ).toFile();
		if( !mods.isDirectory() ) {
			return classes;
		}

		for( File mod : mods.listFiles() ) {
			try {
				if( mod.isDirectory() ) {
					continue;
				}

				JarFile modJar = new JarFile( mod );
				if( modJar.getJarEntry( this.getModPackagePath() ) == null ) {
					continue;
				}

				Enumeration< JarEntry > entries = modJar.entries();
				while( entries.hasMoreElements() ) {
					JarEntry jarEntry = entries.nextElement();
					if( jarEntry.getName().endsWith( ".class" ) && jarEntry.getName().startsWith( this.getModPackagePath() + "/" ) ) {
						Class< ? > clazz = this.tryToLoad( jarEntry.getName().replace( "/", "." ).replace( ".class", "" ) );
						if( clazz != null ) {
							classes.add( clazz );
						}
					}
				}
			} catch( Exception exception ) {
				this.helper.logError( "Failed to find class: %s", exception.toString() );
			}
		}

		return classes;
	}

	private Class< ? > tryToLoad( String name ) throws ClassNotFoundException {
		if( name.contains( "mixin" ) ) {
			return null;
		}

		if( Side.isDedicatedServer() ) {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream( "%s.class".formatted( name.replace( ".", "/" ) ) );
			String bytes = new Scanner( stream ).useDelimiter( "\\A" ).next();
			int startIdx = bytes.indexOf( ".java" );
			int endIdx = bytes.indexOf( "(", startIdx );
			endIdx = bytes.indexOf( ")", endIdx );
			if( startIdx != -1 && endIdx == -1 ) {
				endIdx = bytes.length();
			}
			if( startIdx < endIdx && !Side.canLoadClassOnServer( bytes.substring( startIdx, endIdx ) ) ) {
				return null;
			}
		}

		return Class.forName( name );
	}

	private void addUnique( List< Class< ? > > classes ) {
		this.classes.addAll( classes.stream().filter( clazz->!this.classes.contains( clazz ) ).toList() );
	}
}
