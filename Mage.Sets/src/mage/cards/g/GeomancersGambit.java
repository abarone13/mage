package mage.cards.g;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetCardInLibrary;
import mage.target.common.TargetLandPermanent;

import java.util.UUID;

/**
 * @author TheElk801
 */
public final class GeomancersGambit extends CardImpl {

    public GeomancersGambit(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{2}{R}");

        // Destroy target land. Its controller may search their library for a basic land card, put it onto the battlefield, then shuffle their library.
        this.getSpellAbility().addEffect(new DestroyTargetEffect());
        this.getSpellAbility().addEffect(new GeomancersGambitEffect());
        this.getSpellAbility().addTarget(new TargetLandPermanent());

        // Draw a card.
        this.getSpellAbility().addEffect(new DrawCardSourceControllerEffect(1));
    }

    private GeomancersGambit(final GeomancersGambit card) {
        super(card);
    }

    @Override
    public GeomancersGambit copy() {
        return new GeomancersGambit(this);
    }
}

class GeomancersGambitEffect extends OneShotEffect {

    GeomancersGambitEffect() {
        super(Outcome.PutLandInPlay);
        this.staticText = "Its controller may search their library "
                + "for a basic land card, put it onto the battlefield, "
                + "then shuffle their library";
    }

    private GeomancersGambitEffect(final GeomancersGambitEffect effect) {
        super(effect);
    }

    @Override
    public GeomancersGambitEffect copy() {
        return new GeomancersGambitEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = game.getPermanentOrLKIBattlefield(source.getFirstTarget());
        if (permanent == null) {
            return false;
        }
        Player controller = game.getPlayer(permanent.getControllerId());
        if (controller == null) {
            return false;
        }
        if (!controller.chooseUse(Outcome.PutLandInPlay, "Do you wish to search for a basic land, put it onto the battlefield and then shuffle your library?", source, game)) {
            return true;
        }
        TargetCardInLibrary target = new TargetCardInLibrary(StaticFilters.FILTER_CARD_BASIC_LAND);
        if (controller.searchLibrary(target, source, game)) {
            Card card = controller.getLibrary().getCard(target.getFirstTarget(), game);
            if (card != null) {
                controller.moveCards(card, Zone.BATTLEFIELD, source, game);
            }
        }
        controller.shuffleLibrary(source, game);
        return true;
    }
}
