/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.eldritchmoon;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.condition.common.DeliriumCondition;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.decorator.ConditionalTriggeredAbility;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.LoseLifeTargetEffect;
import mage.abilities.keyword.ReachAbility;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.permanent.token.SpiderToken;
import mage.target.common.TargetOpponent;

/**
 *
 * @author fireshoes
 */
public class IshkanahGrafwidow extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("each Spider you control");

    static {
        filter.add(new SubtypePredicate("Spider"));
    }

    public IshkanahGrafwidow(UUID ownerId) {
        super(ownerId, 162, "Ishkanah, Grafwidow", Rarity.MYTHIC, new CardType[]{CardType.CREATURE}, "{4}{G}");
        this.expansionSetCode = "EMN";
        this.supertype.add("Legendary");
        this.subtype.add("Spider");
        this.power = new MageInt(3);
        this.toughness = new MageInt(5);

        // Reach
        this.addAbility(ReachAbility.getInstance());

        // <i>Delirium</i> &mdash When Ishkanah, Grafwidow enters the battlefield, if there are four or more card types among cards in your graveyard,
        // put three 1/2 green Spider creature tokens with reach onto the battlefield.
        Ability ability = new ConditionalTriggeredAbility(
                new EntersBattlefieldTriggeredAbility(new CreateTokenEffect(new SpiderToken(), 3), false),
                new DeliriumCondition(),
                "<i>Delirium</i> &mdash; When {this} enters the battlefield, if there are four or more card types among cards in your graveyard, "
                + "put three 1/2 green Spider creature tokens with reach onto the battlefield.");
        this.addAbility(ability);

        // {5}{B}: Target opponent loses 1 life for each Spider you control.
        PermanentsOnBattlefieldCount count = new PermanentsOnBattlefieldCount(filter);
        ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new LoseLifeTargetEffect(count), new ManaCostsImpl("{6}{B}"));
        ability.addTarget(new TargetOpponent());
        this.addAbility(ability);
    }

    public IshkanahGrafwidow(final IshkanahGrafwidow card) {
        super(card);
    }

    @Override
    public IshkanahGrafwidow copy() {
        return new IshkanahGrafwidow(this);
    }
}