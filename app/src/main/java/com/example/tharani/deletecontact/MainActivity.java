package com.example.tharani.deletecontact;
/*import is libraries imported for writing the code
* AppCompatActivity is base class for activities
* Bundle handles the orientation of the activity
*/
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /*onCreate is the first method in the life cycle of an activity
   savedInstance passes data to super class,data is pull to store state of application
 * setContentView is used to set layout for the activity
 *R is a resource and it is auto generate file
 * activity_main assign an integer value*/
    private TextView contactId;
    //declaring variables
    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactId =  findViewById(R.id.contact_id);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //if statement tests the condition. It executes the if block if condition is true.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                //  we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            //contactId.setText(String.valueOf(getContactID(getContentResolver(), "8285385442")));
            contactId.setOnClickListener(new View.OnClickListener() {
                //taking OnClickListener for contactId button
                @Override
                public void onClick(View view) {
                    deleteContact();//deletes contact
                }
            });
        }
    }

    @Override
    /**Void is Callback for the result from requesting permissions.
     * onRequestPermissionsResult Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions*/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if statement tests the condition. It executes the if block if condition is true.
                    // permission was granted, Do the
                    // contacts-related task you need to do.
                    // Since reading contacts takes more time, let's run it on a separate thread.
                    //contactId.setText(String.valueOf(getContactID(getContentResolver(), "8285385442")));
                    contactId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteContact();
                        }
                    });
                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "You've denied the required permission.", Toast.LENGTH_LONG);
                    /*A toast is a view containing a quick little message
                LENGTH_LONG Show the view or text notification for a long period of time*/
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /**here it deleteContact*/
    private void deleteContact() {
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.delete_contact_form, null);
        final EditText phone = view.findViewById(R.id.input_contact_phone);
        //taking final which cant be changed
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.delete_contact, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String contactPhone = phone.getText().toString();
                        // Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactPhone));
                        int deleteStatus = ContactHelper.deleteContact(getContentResolver(), contactPhone);
                        if (deleteStatus == 1) {//if statement tests the condition. It executes the if block if condition is true.
                            Toast.makeText(getApplicationContext(), "Deleted successfully.", Toast.LENGTH_LONG).show();
                        } else if(deleteStatus == 0){
                            Toast.makeText(getApplicationContext(), "No such number exists.", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Failed to delete.", Toast.LENGTH_LONG).show();
                            /*A toast is a view containing a quick little message
                LENGTH_LONG Show the view or text notification for a long period of time*/
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_contact, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();//shows dialog
    }
}
