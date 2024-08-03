package com.example.healthpulse;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountHelper {

    private static final String ACCOUNT_TYPE = "com.example";

    public static void addAccount(Context context, String username, String password) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = new Account(username, ACCOUNT_TYPE);

        if (accountManager.addAccountExplicitly(account, password, null)) {
            // Optionally add other data with accountManager.setUserData(account, key, value);
            // Also can add authToken with accountManager.setAuthToken(account, authTokenType, authToken);
        }
    }

    public static String getPassword(Context context, String username) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

        for (Account account : accounts) {
            if (account.name.equals(username)) {
                return accountManager.getPassword(account);
            }
        }
        return null;
    }

    public static boolean accountExists(Context context, String username) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

        for (Account account : accounts) {
            if (account.name.equals(username)) {
                return true;
            }
        }
        return false;
    }
}
