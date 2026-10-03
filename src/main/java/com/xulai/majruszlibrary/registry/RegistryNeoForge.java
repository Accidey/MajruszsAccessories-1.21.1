package com.xulai.majruszlibrary.registry;

import com.xulai.majruszlibrary.annotation.Dist;
import com.xulai.majruszlibrary.annotation.OnlyIn;
import com.xulai.majruszlibrary.modhelper.DataNeoForge;
import com.xulai.majruszlibrary.platform.ModBus;
import com.xulai.majruszlibrary.platform.Side;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryNeoForge implements IRegistryPlatform {
	@Override
	public < Type > void register( RegistryGroup< Type > group ) {
		DataNeoForge data = group.helper.getData( DataNeoForge.class );
		data.lastDeferredRegister = DeferredRegister.create( group.registry.key(), group.helper.getModId() );
		data.lastDeferredRegister.register( ModBus.BUS );
	}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		DataNeoForge data = object.group.helper.getData( DataNeoForge.class );
		DeferredHolder< Type, Type > holder = ( ( DeferredRegister< Type > )data.lastDeferredRegister ).register( object.id, object.newInstance );
		object.set( holder, holder::isBound );
	}

	@Override
	public void register( RegistryCallbacks callbacks ) {
		IEventBus eventBus = ModBus.BUS;
		eventBus.addListener( ( RegisterEvent event )->{
			if( event.getRegistryKey().equals( Registries.TRIGGER_TYPE ) ) {
				callbacks.execute( Custom.Advancements.class, new Custom.Advancements() {
					@Override
					public < Type extends CriterionTrigger< ? > > void register( ResourceLocation id, Type trigger ) {
						CriteriaTriggers.register( id.toString(), trigger );
					}
				} );
			}
		} );
		eventBus.addListener( ( EntityAttributeCreationEvent event )->{
			callbacks.execute( Custom.Attributes.class, event::put );
		} );

		Side.runOnClient( ()->()->{
			eventBus.addListener( ( final RegisterParticleProvidersEvent event )->{
				callbacks.execute( Custom.Particles.class, new CustomParticles( event ) );
			} );
		} );
	}

	@Override
	public IAccessor< Item > getItems() {
		return new Accessor<>( BuiltInRegistries.ITEM );
	}

	@Override
	public IAccessor< MobEffect > getEffects() {
		return new Accessor<>( BuiltInRegistries.MOB_EFFECT );
	}

	@Override
	public IAccessor< Enchantment > getEnchantments() {
		return new Accessor<>( ()->Side.getServer().registryAccess().registryOrThrow( Registries.ENCHANTMENT ) );
	}

	@Override
	public IAccessor< EntityType< ? > > getEntityTypes() {
		return new Accessor<>( BuiltInRegistries.ENTITY_TYPE );
	}

	@Override
	public IAccessor< SoundEvent > getSoundEvents() {
		return new Accessor<>( BuiltInRegistries.SOUND_EVENT );
	}

	@Override
	public Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}

	private static class Accessor< Type > implements IAccessor< Type > {
		private final Supplier< Registry< Type > > registry;

		public Accessor( Registry< Type > registry ) {
			this( ()->registry );
		}

		public Accessor( Supplier< Registry< Type > > registry ) {
			this.registry = registry;
		}

		@Override
		public ResourceLocation getId( Type value ) {
			return this.registry.get().getKey( value );
		}

		@Override
		public Type get( ResourceLocation id ) {
			return this.registry.get().get( id );
		}

		@Override
		public Iterable< Type > get() {
			return this.registry.get();
		}

		@Override
		public Holder< Type > getHolder( Type value ) {
			return this.registry.get().wrapAsHolder( value );
		}

		@Override
		public @NotNull Iterator< Type > iterator() {
			return this.registry.get().iterator();
		}
	}

	@OnlyIn( Dist.CLIENT )
	private static class CustomParticles implements Custom.Particles {
		final RegisterParticleProvidersEvent event;

		public CustomParticles( final RegisterParticleProvidersEvent event ) {
			this.event = event;
		}

		@Override
		public < Type extends ParticleOptions > void register( ParticleType< Type > type, Function< SpriteSet, ParticleProvider< Type > > factory ) {
			this.event.registerSpriteSet( type, ( ParticleEngine.SpriteParticleRegistration< Type > )factory::apply );
		}
	}
}
