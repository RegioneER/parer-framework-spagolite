package it.eng.spagoLite;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import it.eng.spagoIFace.session.SessionCoreManager;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.security.IUser;

public class SessionManager extends SessionCoreManager {

    public static String getCurrentAction(HttpSession httpSession) {
        return (String) httpSession.getAttribute(ACTION_CONTAINER);
    }

    public static void setCurrentAction(HttpSession httpSession, String currentAction) {
        httpSession.setAttribute(ACTION_CONTAINER, currentAction);
    }

    public static MessageBox getMessageBox(HttpSession httpSession) {
        MessageBox messageBox = (MessageBox) httpSession.getAttribute(MESSAGE_CONTAINER);

        if (messageBox == null) {
            messageBox = initMessageBox(httpSession);
        }

        return messageBox;
    }

    public static MessageBox initMessageBox(HttpSession httpSession) {
        MessageBox messageBox = new MessageBox();
        httpSession.setAttribute(MESSAGE_CONTAINER, messageBox);

        return messageBox;
    }

    public static Form getForm(HttpSession httpSession) {
        return (Form) httpSession.getAttribute(FORM_CONTAINER);
    }

    public static void setForm(HttpSession httpSession, Form form) {
        httpSession.setAttribute(FORM_CONTAINER, form);
    }

    public static ArrayList<ExecutionHistory> getExecutionHistory(HttpSession httpSession) {
        ArrayList<ExecutionHistory> list = (ArrayList<ExecutionHistory>) httpSession.getAttribute(NAVHIS_CONTAINER);
        if (list == null) {
            list = initExecutionHistory(httpSession);
        }
        return list;
    }

    private static ArrayList<ExecutionHistory> initExecutionHistory(HttpSession httpSession) {
        ArrayList<ExecutionHistory> list = new ArrayList<ExecutionHistory>();
        httpSession.setAttribute(NAVHIS_CONTAINER, list);

        return list;
    }

    public static ExecutionHistory removeLastExecutionHistory(HttpSession httpSession) {
        ArrayList<ExecutionHistory> list = getExecutionHistory(httpSession);
        if (list.size() > 0) {
            return list.remove(list.size() - 1);
        } else
            return null;

    }

    public static ExecutionHistory getLastExecutionHistory(HttpSession httpSession) {
        ArrayList<ExecutionHistory> list = getExecutionHistory(httpSession);
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else
            return null;
    }

    public static ExecutionHistory setLastExecutionHistory(HttpSession httpSession, ExecutionHistory history) {
        ArrayList<ExecutionHistory> list = getExecutionHistory(httpSession);
        if (list.size() > 0) {
            return list.set(list.size() - 1, history);
        } else
            return null;
    }

    public static void addPrevExecutionToHistory(HttpSession httpSession, boolean isAction, boolean isForward) {
        SessionManager.addPrevExecutionToHistory(httpSession, isAction, isForward, null);
    }

    public static void addPrevExecutionToHistory(HttpSession httpSession, boolean isAction, boolean isForward,
            String params) {
        ExecutionHistory history;
        if (isAction) {
            history = new ExecutionHistory(getCurrentAction(httpSession), getForm(httpSession), isAction, isForward);
            history.setActionPublished(false);
            history.setBackParameter(params != null ? params : "");
            history.setPublisherName(getLastPublisher(httpSession));
        } else {
            history = new ExecutionHistory(getLastPublisher(httpSession), null, isAction, isForward);
        }
        ArrayList<ExecutionHistory> list = getExecutionHistory(httpSession);
        list.add(history);
        httpSession.setAttribute(NAVHIS_CONTAINER, list);
    }

    public static void clearActionHistory(HttpSession httpSession) {
        httpSession.removeAttribute(NAVHIS_CONTAINER);
    }

    public static void setUser(HttpSession httpSession, IUser iUser) {
        if (iUser != null) {
            httpSession.setAttribute(USER_CONTAINER, iUser);
        } else {
            httpSession.removeAttribute(USER_CONTAINER);
        }
    }

    public static IUser<?> getUser(HttpSession httpSession) {
        return (IUser<?>) httpSession.getAttribute(USER_CONTAINER);
    }

    public static void clear(HttpSession httpSession) {
        httpSession.removeAttribute(ACTION_CONTAINER);
        httpSession.removeAttribute(MESSAGE_CONTAINER);
        httpSession.removeAttribute(FORM_CONTAINER);
        httpSession.removeAttribute(USER_CONTAINER);
        httpSession.removeAttribute(NAVHIS_CONTAINER);
    }

    public static void setAll(HttpSession httpSession, String action, Form form, MessageBox messageBox, IUser user) {
        httpSession.setAttribute(ACTION_CONTAINER, action);
        httpSession.setAttribute(MESSAGE_CONTAINER, messageBox);
        httpSession.setAttribute(FORM_CONTAINER, form);
        httpSession.setAttribute(USER_CONTAINER, user);
    }

    public static IUser<?> currentUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            // Object principal = authentication.getPrincipal();
            // return (IUser<?>) (principal instanceof IUser ? principal : null);
            Object details = authentication.getDetails();
            return (IUser<?>) (details instanceof IUser ? details : null);
        }
        return null;
    }

}
