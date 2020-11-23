package work.mgnet.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import work.mgnet.FFA;


public class FFAConfigCommand implements CommandCallable{

	/**
	 * Runned when the Command gets called
	 */
	@Override
	public CommandResult process(CommandSource source, String arguments) throws CommandException {
		// Permission check
		if (!source.hasPermission("mgw.admin")) return CommandResult.builder().successCount(1).affectedEntities(Sponge.getGame().getServer().getOnlinePlayers().size()).build();
		
		String[] args=arguments.split(" "); // Split Arguments
		
		/*
		 * Going trough every Option, and changing them
		 */
		if(args[0].equalsIgnoreCase("pvp")) {
			try {
				FFA.configUtils.setLocation("pvp", Double.parseDouble((args[1])), Double.parseDouble((args[2])), Double.parseDouble((args[3])));
			}catch(NumberFormatException e) {
				throw new CommandException(Text.of("Couldn't parse coordinates"));
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("spawn")) {
			try {
				FFA.configUtils.setLocation("spawn", Double.parseDouble((args[1])), Double.parseDouble((args[2])), Double.parseDouble((args[3])));
			}catch(NumberFormatException e) {
				throw new CommandException(Text.of("Couldn't parse coordinates"));
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("tickrate")) {
			try {
				FFA.configUtils.setFloat("tickrate", Float.parseFloat(args[1]));
			} catch (NumberFormatException e) {
				throw new CommandException(Text.of("Couldn't parse tickrate"));
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("spreadPlayerRadius")){
			try {
				FFA.configUtils.setFloat("spreadPlayerRadius", Float.parseFloat(args[1]));
			} catch (NumberFormatException e) {
				throw new CommandException(Text.of("Couldn't parse radius"));
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("spreadPlayerDistance")) {
			try {
				FFA.configUtils.setFloat("spreadPlayerDistance", Float.parseFloat(args[1]));
			} catch (NumberFormatException e) {
				throw new CommandException(Text.of("Couldn't parse distance"));
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("map")) {
			try {
				FFA.configUtils.setString("map", args[1]);
				FFA.setMapFile(args[1]); // Reload the Map File
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("gamemode")) {
			try {
				FFA.configUtils.setString("gamemode", args[1]);
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}else if(args[0].equalsIgnoreCase("hitdelay")) {
			try {
				FFA.configUtils.setString("hitdelay", args[1]);
			} catch (ObjectMappingException e) {
				throw new CommandException(Text.of("Some unknown mapping error occured"));
			}
		}
		else { // If not in List of Arguments
			// Send Message of Arguments
			source.sendMessage(Text.of("�b� �7/ffa pvp | spawn | tickrate | spreadPlayerRadius | spreadPlayerDistance | map | gamemode | hitdelay")); 
			return CommandResult.builder().successCount(1).build();
		}
		FFA.configUtils.reloadConfiguration(); // Reload the Configuration
		source.sendMessage(Text.of("�b� �7Successfully changed config option "+args[0])); // Send them Message
		return CommandResult.builder().successCount(1).affectedEntities(Sponge.getGame().getServer().getOnlinePlayers().size()).build();
	}
	
	// Stuff noone cares about
	
	@Override
	public List<String> getSuggestions(CommandSource source, String arguments, Location<World> targetPosition)
			throws CommandException {
		List<String> liste= new ArrayList<String>();
		String[] args=arguments.split(" ");
		if(!arguments.contains(" ")) {
			liste.add("pvp");
			liste.add("spawn");
			liste.add("tickrate");
			liste.add("spreadPlayerRadius");
			liste.add("spreadPlayerDistance");
			liste.add("map");
			liste.add("gamemode");
			liste.add("hitdelay");
		} else if (args.length == 1 && args[0].equalsIgnoreCase("map")) {
			for (File file : FFA.getConfigDir().toFile().listFiles()) {
				if (!file.getName().contains(".")) liste.add(file.getName());
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("gamemode")) {
			liste.add("ffa");
			liste.add("teamdeathmatch");
		} else if (args.length == 1 && args[0].equalsIgnoreCase("hitdelay")) {
			liste.add("true");
			liste.add("false");
		}
		return liste;
	}

	@Override
	public boolean testPermission(CommandSource source) {
		return source.hasPermission("mgw.edit");
	}

	@Override
	public Optional<Text> getShortDescription(CommandSource source) {
		return Optional.of(Text.of("Changes configuration"));
	}

	@Override
	public Optional<Text> getHelp(CommandSource source) {
		return Optional.of(Text.of("No help is available atm, please ask your questions after the beep.... *BEEP*"));
	}

	@Override
	public Text getUsage(CommandSource source) {
		return Text.of("/ffa <configname> <value>");
	}
}
