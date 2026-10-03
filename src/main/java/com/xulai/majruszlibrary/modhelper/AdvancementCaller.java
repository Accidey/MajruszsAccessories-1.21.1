package com.xulai.majruszlibrary.modhelper;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xulai.majruszlibrary.registry.Custom;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

class AdvancementCaller implements CriterionTrigger< AdvancementCaller.Instance > {
	final ResourceLocation id;
	final Map< PlayerAdvancements, Set< CriterionTrigger.Listener< Instance > > > players = Maps.newHashMap();

	public AdvancementCaller( ModHelper helper ) {
		this.id = helper.getLocation( "basic_trigger" );

		helper.create( Custom.Advancements.class, advancements->advancements.register( this.id, this ) );
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public void addPlayerListener( PlayerAdvancements playerAdvancements, CriterionTrigger.Listener< Instance > listener ) {
		this.players.computeIfAbsent( playerAdvancements, key->Sets.newHashSet() ).add( listener );
	}

	@Override
	public void removePlayerListener( PlayerAdvancements playerAdvancements, CriterionTrigger.Listener< Instance > listener ) {
		Set< CriterionTrigger.Listener< Instance > > listeners = this.players.get( playerAdvancements );
		if( listeners != null ) {
			listeners.remove( listener );
			if( listeners.isEmpty() ) {
				this.players.remove( playerAdvancements );
			}
		}
	}

	@Override
	public void removePlayerListeners( PlayerAdvancements playerAdvancements ) {
		this.players.remove( playerAdvancements );
	}

	@Override
	public Codec< Instance > codec() {
		return Instance.CODEC;
	}

	public void trigger( ServerPlayer player, String achievementId ) {
		PlayerAdvancements playerAdvancements = player.getAdvancements();
		Set< CriterionTrigger.Listener< Instance > > listeners = this.players.get( playerAdvancements );
		if( listeners == null ) {
			return;
		}

		for( CriterionTrigger.Listener< Instance > listener : Set.copyOf( listeners ) ) {
			if( listener.trigger().achievementId.equals( achievementId ) ) {
				listener.run( playerAdvancements );
			}
		}
	}

	public record Instance( Optional< ContextAwarePredicate > player, String achievementId ) implements CriterionTriggerInstance {
		public static final Codec< Instance > CODEC = RecordCodecBuilder.create( instance->instance.group(
			ContextAwarePredicate.CODEC.optionalFieldOf( "player" ).forGetter( Instance::player ),
			Codec.STRING.fieldOf( "type" ).forGetter( Instance::achievementId )
		).apply( instance, Instance::new ) );

		@Override
		public Optional< ContextAwarePredicate > player() {
			return this.player;
		}

		@Override
		public void validate( net.minecraft.advancements.critereon.CriterionValidator validator ) {
			validator.validateEntity( this.player, "player" );
		}
	}
}
