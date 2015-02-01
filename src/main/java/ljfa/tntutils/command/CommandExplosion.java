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
        return "explosion <x> <y> <z> [strength] [damage blocks] [set fires]";
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
        
        float strength = args.length >= 4 ? (float)parseDouble(sender, args[3]) : 4.0f;
        boolean blockDmg = args.length >= 5 ? parseBoolean(sender, args[4]) : true;
        boolean fire = args.length >= 6 ? parseBoolean(sender, args[5]) : false;
        
        world.newExplosion(null, x, y, z, strength, fire, blockDmg);
    }

}
