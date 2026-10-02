package net.arcana.addons.ability;

public interface Ability {

    String getId();

    void execute(AbilityContext context);
}