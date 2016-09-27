/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mage.abilities.keyword;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.common.DiesTriggeredAbility;
import mage.abilities.condition.common.SourceHasCounterCondition;
import mage.abilities.decorator.ConditionalTriggeredAbility;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.AsThoughEffectImpl;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.effects.common.counter.RemoveCounterSourceEffect;
import mage.cards.Card;
import mage.constants.AsThoughEffectType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;

/**
 *
 * @author Styxo
 */
public class RepairAbility extends DiesTriggeredAbility {

    private String ruleText;

    public RepairAbility(int count) {
        super(new AddCountersSourceEffect(CounterType.REPAIR.createInstance(), new StaticValue(count), false, true));
        addSubAbility(new RepairBeginningOfUpkeepTriggeredAbility());
        addSubAbility(new RepairCastFromGraveyardTriggeredAbility());
        
        StringBuilder sb = new StringBuilder("Repair ");
        sb.append(count)
                .append(" <i>(When this creature dies, put ")
                .append(count)
                .append(" repair counters on it. At the beggining of your upkeep, remove a repair counter. Whenever the last is removed, you may cast it from graveyard until end of turn.)</i>");
        ruleText = sb.toString();
    }

    public RepairAbility(final RepairAbility ability) {
        super(ability);
        this.ruleText = ability.ruleText;
    }

    @Override
    public String getRule() {
        return ruleText;
    }

}

class RepairCastFromGraveyardEffect extends AsThoughEffectImpl {

    public RepairCastFromGraveyardEffect() {
        super(AsThoughEffectType.PLAY_FROM_NOT_OWN_HAND_ZONE, Duration.EndOfTurn, Outcome.Benefit);
        staticText = "You may cast it from graveyard until end of turn";
    }

    public RepairCastFromGraveyardEffect(final RepairCastFromGraveyardEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public RepairCastFromGraveyardEffect copy() {
        return new RepairCastFromGraveyardEffect(this);
    }

    @Override
    public boolean applies(UUID objectId, Ability source, UUID affectedControllerId, Game game) {
        return source.getControllerId().equals(affectedControllerId);
    }
}

class RepairCastFromGraveyardTriggeredAbility extends TriggeredAbilityImpl {

    public RepairCastFromGraveyardTriggeredAbility() {
        super(Zone.GRAVEYARD, new RepairCastFromGraveyardEffect());
        setRuleVisible(false);
    }

    public RepairCastFromGraveyardTriggeredAbility(RepairCastFromGraveyardTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.COUNTER_REMOVED;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getTargetId().equals(getSourceId())) {
            Card card = game.getCard(getSourceId());
            if (card != null && game.getState().getZone(card.getId()) == Zone.GRAVEYARD
                    && card.getCounters(game).getCount(CounterType.REPAIR) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever the last repair counter is removed, you may cast {this} from your graveyard until end of turn";
    }

    @Override
    public RepairCastFromGraveyardTriggeredAbility copy() {
        return new RepairCastFromGraveyardTriggeredAbility(this);
    }
}

class RepairBeginningOfUpkeepTriggeredAbility extends ConditionalTriggeredAbility {

    public RepairBeginningOfUpkeepTriggeredAbility() {
        super(new BeginningOfUpkeepTriggeredAbility(Zone.GRAVEYARD, new RemoveCounterSourceEffect(CounterType.REPAIR.createInstance()), TargetController.YOU, false),
                new SourceHasCounterCondition(CounterType.REPAIR),
                "At the beginning of your upkeep, remove a repair counter from {this}");
        this.setRuleVisible(false);

    }

    public RepairBeginningOfUpkeepTriggeredAbility(final RepairBeginningOfUpkeepTriggeredAbility effect) {
        super(effect);
    }

    @Override
    public RepairBeginningOfUpkeepTriggeredAbility copy() {
        return new RepairBeginningOfUpkeepTriggeredAbility(this);
    }
}