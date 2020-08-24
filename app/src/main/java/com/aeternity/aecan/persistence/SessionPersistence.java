package com.aeternity.aecan.persistence;

import com.aeternity.aecan.helpers.DialogInterface;
import com.aeternity.aecan.network.requests.RefreshRequest;
import com.aeternity.aecan.network.responses.LoginResponse;
import com.aeternity.aecan.util.Constants.GrantType;

import java.util.ArrayList;

import io.paperdb.Paper;

public class SessionPersistence {

    private static final String PAPER_KEY_SESSION = "PAPER_KEY_SESSION";
    private static ArrayList<DialogInterface> dialogInterfaces;

    public static void initObserver(){
        dialogInterfaces = new ArrayList<>();
    }
    public static LoginResponse newOrReadFromPaper() {
        if (Paper.book().contains(PAPER_KEY_SESSION)) {
            return Paper.book().read(PAPER_KEY_SESSION);
        } else {
            Paper.book().write(PAPER_KEY_SESSION, new LoginResponse());
            return Paper.book().read(PAPER_KEY_SESSION);
        }
    }

    public static LoginResponse readFromPaper() {
        return Paper.book().read(PAPER_KEY_SESSION, null);
    }

    public static boolean hasSession(){
        return Paper.book().contains(PAPER_KEY_SESSION);
    }

    public static void deleteSessionFromPersistence() {
        Paper.book().delete(PAPER_KEY_SESSION);
    }

    public static void saveSessionInPersistence(LoginResponse session) {
        Paper.book().write(PAPER_KEY_SESSION, session);
    }

    public static RefreshRequest getRefreshRequest() {
        LoginResponse login = readFromPaper();
        if (login != null)
            return new RefreshRequest(login.getRefreshToken(), GrantType.REFRESH_TOKEN.getText());
        return null;
    }


    public static void subscribe(DialogInterface dialogInterface){
        dialogInterfaces.add(dialogInterface);
    }

    public static void unsubscribe(DialogInterface dialogInterface){
        dialogInterfaces.remove(dialogInterface);
    }
    public static void notifySuccessObserver(){
        for (DialogInterface dialogInterface: dialogInterfaces){
            dialogInterface.onDialogSuccessRequest();
        }
    }

    public static void notifyFailObserver(String message){
        for (DialogInterface dialogInterface: dialogInterfaces){
            dialogInterface.onDialogFailRequest(message);
        }
    }
}
