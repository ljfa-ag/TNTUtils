package ljfa.tntutils.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.world.World;

public class CommandExplosion extends CommandBase {
    @Override
    public String getCommandName() {
        return "explosion";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "explosion <x> <y> <z> [strength]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length < 3)
            throw new WrongUsageException("Too few arguments", new Object[0]);
        
        World world = sender.getEntityWorld();
        double x = (double)sender.getPlayerCoordinates().posX + 0.5;
        double y = (double)sender.getPlayerCoordinates().posY;
        double z = (double)sender.getPlayerCoordinates().posZ + 0.5;
        
        x = func_110666_a(sender, x, args[0]);
        y = func_110666_a(sender, y, args[1]);
        z = func_110666_a(sender, z, args[2]);
        
        float strength = 4.0f;
        if(args.length >= 4)
            strength = Float.parseFloat(args[3]);
        
        world.createExplosion(null, x, y, z, strength, true);
    }

}
