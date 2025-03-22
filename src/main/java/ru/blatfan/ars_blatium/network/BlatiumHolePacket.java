package ru.blatfan.ars_blatium.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.joml.Vector3f;
import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.client.particle.ParticleBuilder;
import ru.blatfan.blatapi.fluffy_fur.client.particle.data.ColorParticleData;
import ru.blatfan.blatapi.fluffy_fur.client.particle.data.GenericParticleData;
import ru.blatfan.blatapi.fluffy_fur.client.screenshake.ScreenshakeHandler;
import ru.blatfan.blatapi.fluffy_fur.client.screenshake.ScreenshakeInstance;
import ru.blatfan.blatapi.fluffy_fur.client.shader.postprocess.GlowPostProcess;
import ru.blatfan.blatapi.fluffy_fur.client.shader.postprocess.GlowPostProcessInstance;
import ru.blatfan.blatapi.fluffy_fur.client.shader.postprocess.PostProcess;
import ru.blatfan.blatapi.fluffy_fur.common.easing.Easing;
import ru.blatfan.blatapi.fluffy_fur.common.network.PositionClientPacket;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurParticles;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurRenderTypes;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatium.Blatium;

import java.util.function.Supplier;

public class BlatiumHolePacket extends PositionClientPacket {
    public BlatiumHolePacket(double x, double y, double z) {
        super(x, y, z);
    }
    public BlatiumHolePacket(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Level level = FluffyFur.proxy.getLevel();
        ParticleBuilder.create(FluffyFurParticles.WISP)
            .setColorData(ColorParticleData.create(ColorHelper.getColor(Blatium.COLOR_BLATIUM)).build())
            .setLifetime(60).setRenderType(FluffyFurRenderTypes.TRANSLUCENT_PARTICLE)
            .setTransparencyData(GenericParticleData.create(0.5F, 0.0F).setEasing(Easing.QUARTIC_OUT).build())
            .setScaleData(GenericParticleData.create(0.3F, 2.0F, 0.0F).setEasing(Easing.ELASTIC_OUT).build())
            .randomVelocity(0.34).repeat(level, x, y+1, z, 100);
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(60).setIntensity(1.0F, 0.0F).setEasing(Easing.QUINTIC_IN_OUT));
    }
    
    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, BlatiumHolePacket.class, BlatiumHolePacket::encode, BlatiumHolePacket::decode, BlatiumHolePacket::handle);
    }
    
    private static BlatiumHolePacket decode(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new BlatiumHolePacket(x, y, z);
    }
}
