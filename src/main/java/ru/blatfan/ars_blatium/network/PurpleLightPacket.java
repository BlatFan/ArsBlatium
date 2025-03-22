package ru.blatfan.ars_blatium.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.joml.Vector3f;
import ru.blatfan.blatapi.fluffy_fur.client.shader.postprocess.GlowPostProcess;
import ru.blatfan.blatapi.fluffy_fur.client.shader.postprocess.GlowPostProcessInstance;
import ru.blatfan.blatapi.fluffy_fur.common.network.PositionClientPacket;

import java.util.function.Supplier;

public class PurpleLightPacket extends PositionClientPacket {
    public PurpleLightPacket(double x, double y, double z) {
        super(x, y, z);
    }
    public PurpleLightPacket(BlockPos pos) {
        super(pos);
    }
    
    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Vec3 pos = new Vec3(x,y,z);
        GlowPostProcess.INSTANCE.addInstance((new GlowPostProcessInstance(pos.toVector3f(), new Vector3f(0.55f,0.37f,0.93f)))
            .setRadius(5.5F).setIntensity(2.5F).setFadeTime(40));
    }
    
    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PurpleLightPacket.class, PurpleLightPacket::encode, PurpleLightPacket::decode, PurpleLightPacket::handle);
    }
    
    private static PurpleLightPacket decode(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new PurpleLightPacket(x, y, z);
    }
}