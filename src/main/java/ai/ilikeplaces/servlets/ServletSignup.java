package ai.ilikeplaces.servlets;

import ai.ilikeplaces.CrudServiceLocal;
import ai.ilikeplaces.SBLoggedOnUserFace;
//import ai.ilikeplaces.entities.Human.GenderCode.IllegalEnumValueException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentications;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.security.blowfish.jbcrypt.BCrypt;
import ai.ilikeplaces.security.face.SingletonHashingFace;
import ai.ilikeplaces.servlets.Controller.Page;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Ravindranath Akila
 */
final public class ServletSignup extends HttpServlet {

    final Logger logger = Logger.getLogger(ServletSignup.class.getName());
    final static private String Username = "Username";
    final static private String Password = "Password";
    final static private String Email = "Email";
    final static private String Gender = "Gender";
    final static private String DateOfBirth = "DateOfBirth";
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Human> crudServiceLocal_ = null;
    private SingletonHashingFace singletonHashingFace = null;
    private CrudServiceLocal<HumansAuthentications> crudServiceLocalHuman_ = null;

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {
                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                context = new InitialContext(p_);
                if (context == null) {
                    log.append("\nVARIABLE context IS NULL! ");
                    log.append(context);
                    break init;
                }

                crudServiceLocal_ = (CrudServiceLocal) context.lookup("CrudServiceLocal");
                if (crudServiceLocal_ == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceLocal_);
                    break init;
                }

                crudServiceLocalHuman_ = (CrudServiceLocal) context.lookup("CrudServiceLocal");
                if (crudServiceLocalHuman_ == null) {
                    log.append("\nVARIABLE crudServiceLocalHuman_ IS NULL! ");
                    log.append(crudServiceLocalHuman_);
                    break init;
                }

                singletonHashingFace = (SingletonHashingFace) context.lookup("SingletonHashingLocal");
                if (singletonHashingFace == null) {
                    log.append("\nVARIABLE singletonHashingFace IS NULL! ");
                    log.append(singletonHashingFace);
                    break init;
                }
            } catch (NamingException ex) {
                log.append("\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!");
                logger.log(Level.SEVERE, "\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!", ex);
                break init;
            }

            /**
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ExceptionConstructorInvokation(log.toString());
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request__
     * @param response__
     * @throws ServletException
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request__, final HttpServletResponse response__)
            throws ServletException, IOException {

        response__.setContentType("text/html;charset=UTF-8");

        /**
         * Make user session anyway as he camed to log in
         */
        final HttpSession userSession_ = request__.getSession();

        /**
         * Set a timeout compatible with the stateful session bean handling user
         */
        userSession_.setMaxInactiveInterval(10);

        final Enumeration enumerated = request__.getParameterNames();
        logger.log(Level.SEVERE, "PARAMETERs");
        while (enumerated.hasMoreElements()) {
            String param = (String) enumerated.nextElement();
            logger.info("PARAMETER NAME:" + param);
            logger.info("VALUE:" + request__.getParameter(param));

        }
        final String username = request__.getParameter(Username);
        final String password = request__.getParameter(Password);
        final String email = request__.getParameter(Email);
        final String gender = request__.getParameter(Gender);
        final String dateOfBirth = request__.getParameter(DateOfBirth);

        processSignup:
        {
            userSession_.removeAttribute(ServletLogin.SBLoggedOnUser);
            if (username != null && password != null) {// && email != null && gender != null && dateOfBirth != null) {

                if (crudServiceLocal_.find(Human.class, username) == null) {
                    final Human newUser = new Human();
                    newUser.setHumanId(username);
                    final HumansAuthentications ha = new HumansAuthentications();
                    ha.setHumanId(newUser.getHumanId());
                    ha.setHumanAuthenticationSalt(BCrypt.gensalt());
                    ha.setHumanAuthenticationHash(singletonHashingFace.getHash(password, ha.getHumanAuthenticationSalt()));
                    newUser.setHumansAuthentications(ha);
                    newUser.setHumanAlive(true);
                    final HumansIdentity hid = new HumansIdentity();
                    hid.setHumanId(newUser.getHumanId());
                    newUser.setHumansIdentity(hid);
                    crudServiceLocal_.create(newUser);

                    try {
                        final SBLoggedOnUserFace sBLoggedOnUserFace = (SBLoggedOnUserFace) context.lookup("SBLoggedOnUserLocal");
                        final Human loggedOnUser = crudServiceLocal_.find(Human.class, username);
                        sBLoggedOnUserFace.setLoggedOnUserId(loggedOnUser.getHumanId());
                        userSession_.setAttribute(ServletLogin.SBLoggedOnUser, sBLoggedOnUserFace);
                        final Page home = Page.home;
                        response__.sendRedirect(home.toString());
                        break processSignup;
                    } catch (NamingException ex) {
                        logger.log(Level.SEVERE, "SORRY! COULD NOT ADD STATEFUL SESSION BEAN TO THE SESSION!", ex);
                        final Page login = Page.login;
                        response__.sendRedirect(login.toString());
                        break processSignup;
                    }
                } else {
                    logger.log(Level.INFO, "SORRY! A USER IN THAT NAME ALREADY EXISTS!:" + username);
                    request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
                    break processSignup;
                }
            } else {
                request__.getRequestDispatcher("/signup.jsp").forward(request__, response__);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}