package ai.baby.logic.Listeners;

import ai.baby.logic.Listeners.widgets.SignInOn;
import ai.baby.logic.Listeners.widgets.SignInOnCriteria;
import ai.baby.logic.Listeners.widgets.subscribe.Subscribe;
import ai.baby.logic.Listeners.widgets.subscribe.SubscribeCriteria;
import ai.baby.servlets.Controller;
import ai.baby.util.AbstractListener;
import ai.baby.util.Loggers;
import ai.baby.util.SmartLogger;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.baby.rbs.RBGet;
import ai.scribble._todo;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_todo(task = "rename to listenerlocation. do a string search on listenermain to find usage first. current search shows no issues. refac delayed till next check")
public class ListenerAarrr implements ItsNatServletRequestListener {
// ------------------------------ FIELDS ------------------------------

    final static protected String LocationId = RBGet.globalConfig.getString("LOCATIONID");

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ItsNatServletRequestListener ---------------------

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {
        new AbstractListener(request__, response__) {
//        new AbstractSkeletonListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");


                new SignInOn(request__, $(Controller.Page.AarrrHeader),
                        new SignInOnCriteria()
                                .setHumanId(new HumanId(getUsername()))
                                .setSignInOnDisplayComponent(SignInOnCriteria.SignInOnDisplayComponent.HOME)) {
                };


                new Subscribe(request__, new SubscribeCriteria().setHumanId(new HumanId(getUsername())), $(Controller.Page.AarrrSubscribe));

                if (getUsername() != null) {
                    displayNone($(Controller.Page.AarrrJuice));
                    displayBlock($(Controller.Page.AarrrDownTownHeatMap));
                } else {
                }


                SmartLogger.g().complete(Loggers.LEVEL.SERVER_STATUS, Loggers.DONE);
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {

            }
        };
    }
}
