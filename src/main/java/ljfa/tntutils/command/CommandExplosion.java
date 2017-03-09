package ljfa.tntutils.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommandExplosion extends CommandBase {
    @Override
    public String getName() {
        return "explosion";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "tnt_utilities.commands.explosion.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 3)
            throw new WrongUsageException(getUsage(sender));
        
        World world = sender.getEntityWorld();
        double x = sender.getPositionVector().xCoord;
        double y = sender.getPositionVector().yCoord;
        double z = sender.getPositionVector().zCoord;
        
        CoordinateArg xArg = parseCoordinate(x, args[0], true);
        CoordinateArg yArg = parseCoordinate(y, args[1], 0, 0, false);
        CoordinateArg zArg = parseCoordinate(z, args[2], true);
        
        float strength   = args.length >= 4 ? (float)parseDouble(args[3]) : 4.0f;
        boolean blockDmg = args.length >= 5 ? parseBoolean(args[4])       : true;
        boolean fire     = args.length >= 6 ? parseBoolean(args[5])       : false;

        Entity source = (sender instanceof Entity) ? (Entity)sender : null;
        
        world.newExplosion(source, xArg.getResult(), yArg.getResult(), zArg.getResult(), strength, fire, blockDmg);
    }

}
