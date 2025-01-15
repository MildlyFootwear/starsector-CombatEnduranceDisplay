package Shoey.CombatEnduranceDisplay;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.skills.CombatEndurance;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.List;

public class DisplayScript extends BaseEveryFrameCombatPlugin {

    CombatEngineAPI engineAPI = null;

    @Override
    public void init(CombatEngineAPI engine) {
        super.init(engine);
        engineAPI = engine;
    }

    float getRepaired(String key) {
        Float r = (Float) Global.getCombatEngine().getCustomData().get(key);
        if (r == null) r = 0f;
        return r;
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);
        if (engineAPI == null)
            return;

        ShipAPI playerShip = engineAPI.getPlayerShip();

        if (playerShip == null || playerShip.getCaptain() == null)
            return;

        float r1 = getRepaired("CombatEnduranceRegen_ " + playerShip.getId() + "_repaired");
        float r2 = getRepaired("CombatEnduranceRegen_ " + playerShip.getCaptain().getId() + "_repaired");
        float repaired = Math.max(r1, r2);

        float limit = Math.max(CombatEndurance.TOTAL_REGEN_MAX_POINTS, playerShip.getMaxHitpoints() * CombatEndurance.TOTAL_REGEN_MAX_HULL_FRACTION);

        if (repaired == 0)
            return;

        if (repaired >= limit)
        {
            engineAPI.maintainStatusForPlayerShip("CombatEnduranceRegenDisplay", "graphics/ui/icons/icon_repair_refit.png", "Combat Endurance", "Can not field repair", false);
            return;
        }

        engineAPI.maintainStatusForPlayerShip("CombatEnduranceRegenDisplay", "graphics/ui/icons/icon_repair_refit.png", "Combat Endurance", Math.round(repaired) + " out of " + Math.round(limit) + " repaired", false);

    }
}
