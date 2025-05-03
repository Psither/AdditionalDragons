package by.psither.dragonsurvival.client.particles.SeaDragon;

import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.DragonParticle;
import by.psither.dragonsurvival.common.particles.SmallGlowSlimeParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;

public class SmallGlowSlimeParticle extends DragonParticle {
    public SmallGlowSlimeParticle(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls, SpriteSet sprite){
        super(world, x, y, z, vX, vY, vZ, duration, swirls, sprite);
    }

    public static final class Factory implements ParticleProvider<SmallGlowSlimeParticleOption> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite){ spriteSet = sprite; }

        @Override
        public Particle createParticle(SmallGlowSlimeParticleOption type, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
            SmallGlowSlimeParticle particle = new SmallGlowSlimeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, type.duration(), type.swirls(), spriteSet);
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }
}
