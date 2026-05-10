package me.armar.plugins.utils.pluginlibrary.hooks;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.registry.GlobalRegistry;
import dev.aurelium.auraskills.api.registry.NamespacedId;
import dev.aurelium.auraskills.api.skill.Skill;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.user.SkillsUser;
import me.armar.plugins.autorank.util.AutorankTools;
import me.armar.plugins.utils.pluginlibrary.Library;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;

import java.util.Locale;

import static java.lang.Boolean.TRUE;

public class AuraSkillsHook extends LibraryHook {

    public boolean isHooked() {
        return isPluginAvailable(Library.AURA_SKILLS);
    }

    public boolean hook() {
        return isPluginAvailable(Library.AURA_SKILLS);
    }

    //   AuraSkillsApi auraSkills = AuraSkillsApi.get();

    public double getStatLevel(Player player, String statType) {
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        if (!isPluginAvailable(Library.AURA_SKILLS)) {
            return 0.0F;
        } else {
            statType = statType.toUpperCase(Locale.ROOT);
            SkillsUser user = auraSkills.getUser(player.getUniqueId());
            GlobalRegistry registry = auraSkills.getGlobalRegistry();
            Stats stat = (Stats) registry.getStat(NamespacedId.of("AuraSkills", statType));
            //   stat = Stats.valueOf("Stats." + statType);
            return 0.0F; // user.getStatLevel(stat);
        }
    }

    public int getSkillLevel(Player player, String skillName) {
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        if (!isPluginAvailable(Library.AURA_SKILLS)) {
            return 0;
        } else {
            skillName = skillName.toUpperCase(Locale.ROOT);
            SkillsUser user = auraSkills.getUser(player.getUniqueId());
            GlobalRegistry registry = auraSkills.getGlobalRegistry();
            Skill skill = registry.getSkill(NamespacedId.of("AuraSkills", skillName));
          //  skill = Skills.valueOf("Skills." + skillName);
            return user.getSkillLevel(skill);
        }
    }


    public double getXP(Player player, String skillName) {
        if (!isPluginAvailable(Library.AURA_SKILLS)) {
            return 0.0F;
        } else {
            AuraSkillsApi auraSkills = AuraSkillsApi.get();
            Skills skills = null;
            skills = Skills.valueOf("Stats." + skillName.toUpperCase(Locale.ROOT));
            SkillsUser user = auraSkills.getUser(player.getUniqueId());
            return 0.0F; // user.getSkillXp(skills);
        }
    }

    public double getMana(Player player) {
        if (this.isHooked() == TRUE) {
            AuraSkillsApi auraSkills = AuraSkillsApi.get();
            SkillsUser user = auraSkills.getUser(player.getUniqueId());
            double mana = user.getMana();
            return mana;
        } else {
            return 0.0F;
        }
    }
}
