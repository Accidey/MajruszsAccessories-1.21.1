package com.xulai.majruszlibrary.item;

import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.registry.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EnchantmentHelper {
	public static int getLevel( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation enchantmentId = Registries.ENCHANTMENTS.getId( enchantment.get() );
		for( EnchantmentDef enchantmentDef : EnchantmentHelper.read( itemStack ).enchantments ) {
			if( enchantmentId.equals( enchantmentDef.id ) ) {
				return enchantmentDef.level;
			}
		}

		return 0;
	}

	public static int getLevel( Supplier< ? extends Enchantment > enchantment, LivingEntity entity ) {
		int level = 0;
		for( ItemStack itemStack : enchantment.get().getSlotItems( entity ).values() ) {
			level = Math.max( level, EnchantmentHelper.getLevel( enchantment, itemStack ) );
		}

		return level;
	}

	public static int getLevelSum( Supplier< ? extends Enchantment > enchantment, Iterable< ItemStack > itemStacks ) {
		int sum = 0;
		for( ItemStack itemStack : itemStacks ) {
			sum += EnchantmentHelper.getLevel( enchantment, itemStack );
		}

		return sum;
	}

	public static int getLevelSum( Supplier< ? extends Enchantment > enchantment, LivingEntity entity ) {
		int sum = 0;
		for( ItemStack itemStack : enchantment.get().getSlotItems( entity ).values() ) {
			sum += EnchantmentHelper.getLevel( enchantment, itemStack );
		}

		return sum;
	}

	public static int getLevelSum( Supplier< ? extends Enchantment > enchantment, LivingEntity entity, Iterable< EquipmentSlot > slots ) {
		int sum = 0;
		for( EquipmentSlot slot : slots ) {
			sum += EnchantmentHelper.getLevel( enchantment, entity.getItemBySlot( slot ) );
		}

		return sum;
	}

	public static boolean has( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		return EnchantmentHelper.getLevel( enchantment, itemStack ) > 0;
	}

	public static boolean has( Supplier< ? extends Enchantment > enchantment, LivingEntity entity ) {
		return EnchantmentHelper.getLevel( enchantment, entity ) > 0;
	}

	public static boolean increaseLevel( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation id = Registries.ENCHANTMENTS.getId( enchantment.get() );
		EnchantmentsDef enchantmentsDef = EnchantmentHelper.read( itemStack );
		for( EnchantmentDef enchantmentDef : enchantmentsDef.enchantments ) {
			if( id.equals( enchantmentDef.id ) ) {
				if( enchantmentDef.level >= enchantment.get().getMaxLevel() ) {
					return false;
				} else {
					enchantmentDef.level++;
					ItemHelper.modifyTag( itemStack, tag -> Serializables.write( enchantmentsDef, tag ) );
					return true;
				}
			}
		}

		enchantmentsDef.enchantments.add( new EnchantmentDef( id, 1 ) );
		ItemHelper.modifyTag( itemStack, tag -> Serializables.write( enchantmentsDef, tag ) );
		return true;
	}

	public static boolean remove( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation id = Registries.ENCHANTMENTS.getId( enchantment.get() );
		EnchantmentsDef enchantmentsDef = EnchantmentHelper.read( itemStack );
		for( int idx = 0; idx < enchantmentsDef.enchantments.size(); ++idx ) {
			if( id.equals( enchantmentsDef.enchantments.get( idx ).id ) ) {
				enchantmentsDef.enchantments.remove( idx );
				ItemHelper.modifyTag( itemStack, tag -> Serializables.write( enchantmentsDef, tag ) );
				return true;
			}
		}

		return false;
	}

	public static EnchantmentsDef read( ItemStack itemStack ) {
		EnchantmentsDef enchantmentsDef = new EnchantmentsDef();
		CompoundTag tag = ItemHelper.getTag( itemStack );

		return ItemHelper.hasTag( itemStack ) ? Serializables.read( enchantmentsDef, tag ) : enchantmentsDef;
	}

	public static class EnchantmentsDef {
		static {
			Serializables.get( EnchantmentsDef.class )
				.define( "Enchantments", Reader.list( Reader.custom( EnchantmentDef::new ) ), s->s.enchantments, ( s, v )->s.enchantments = v );
		}

		public List< EnchantmentDef > enchantments = new ArrayList<>();
	}

	public static class EnchantmentDef {
		static {
			Serializables.get( EnchantmentDef.class )
				.define( "id", Reader.location(), s->s.id, ( s, v )->s.id = v )
				.define( "lvl", Reader.integer(), s->s.level, ( s, v )->s.level = v );
		}

		public ResourceLocation id;
		public int level;

		public EnchantmentDef( ResourceLocation id, int level ) {
			this.id = id;
			this.level = level;
		}

		public EnchantmentDef() {}
	}
}
