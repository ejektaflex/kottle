package net.alexwells.kottle

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import java.util.function.Supplier

/**
 * Annotate a class which will be subscribed to an Event Bus at mod construction time.
 * Defaults to subscribing the current modid to the [MinecraftForge.EVENT_BUS]
 * on both sides.
 *
 * @see Bus
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class KotlinEventBusSubscriber(
        /**
         * Specify targets to load this event subscriber on. Can be used to avoid loading Client specific events
         * on a dedicated server, for example.
         *
         * @return an array of Dist to load this event subscriber on
         */
        vararg val value: Dist = [Dist.CLIENT, Dist.DEDICATED_SERVER],

        /**
         * Optional value, only necessary if this annotation is not on the same class that has a @Mod annotation.
         * Needed to prevent early classloading of classes not owned by your mod.
         * @return a modid
         */
        val modid: String = "",

        /**
         * Specify an alternative bus to listen to
         *
         * @return the bus you wish to listen to
         */
        val bus: Bus = Bus.FORGE
) {
    enum class Bus private constructor(private val busSupplier: Supplier<IEventBus>) {
        /**
         * The main Forge Event Bus.
         * @see MinecraftForge.EVENT_BUS
         */
        FORGE(Supplier { MinecraftForge.EVENT_BUS }),

        /**
         * The mod specific Event bus.
         * @see FMLKotlinModLoadingContext.get().modEventBus
         */
        MOD(Supplier { FMLKotlinModLoadingContext.get().modEventBus });

        fun bus(): Supplier<IEventBus> {
            return busSupplier
        }
    }
}