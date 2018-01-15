package com.example.tharani.deletecontact;
/*import is libraries imported for writing the code
* AppCompatActivity is base class for activities
* Bundle handles the orientation of the activity
*/
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
//created class ContactHelper
public class ContactHelper {
    public static int deleteContact(ContentResolver contactHelper, String number) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //taken new array list object
        String contactID = String.valueOf(getContactID(contactHelper, number));
        Log.d("Contact ID: ", contactID);
         /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
        String[] args = new String[]{contactID};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {//taking try block
            ContentProviderResult[] contentProviderResult = contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d("CP: ", String.valueOf(contentProviderResult[0].count));
            /*here taking Log.e method to write logs and displaying
            * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
            return contentProviderResult[0].count;
        } catch (RemoteException e) {//catches exception
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();//It prints the stack trace of the Exception to System

        }

        return -1;//returns false
    }
    //gets contact id using get method
    private static long getContactID(ContentResolver contactHelper, String number) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = {ContactsContract.PhoneLookup._ID};
        Cursor cursor = null;//cursor sets to null

        try {
            cursor = contactHelper.query(contactUri, projection, null, null, null);
            if (cursor != null) {//if statement tests the condition. It executes the if block if condition is true.
                while (cursor.moveToNext()) {//taking while lop
                    long personID = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                    return personID;//returns personID
                }
                cursor.close();//closing cursor
            } else {
                Log.d("Contact ID: ", "null");
                /*here taking Log.e method to write logs and displaying
                        * tag Used to identify the source of a log message, usually identifies the class or activity where the log call occurs.*/
            }
            return -1;//returns null
        } catch (Exception e) {//catches exception
            e.printStackTrace();//It prints the stack trace of the Exception to System
        } finally {
            if (cursor != null) {//if statement tests the condition. It executes the if block if condition is true.
                cursor.close();//closes
                cursor = null;//cursor sets to null
            }
        }
        return -1;//returns null
    }
}
