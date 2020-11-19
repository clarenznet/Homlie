package com.luns.neuro.mlkn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.luns.neuro.mlkn.library.ConnectionDetector
import com.luns.neuro.mlkn.library.SharedPrefManager
import com.luns.neuro.mlkn.library.User
import kotlinx.android.synthetic.main.signin.*
import java.util.*

class  SignIn : AppCompatActivity() {

    //Defined the required values
    companion object {
        const val CHANNEL_ID = "simplified_coding"
        private const val CHANNEL_NAME= "Simplified Coding"
        private const val CHANNEL_DESC = "Android Push Notification Tutorial"
        private val RC_SIGN_IN = 101
        private var firebaseid:String?=null
        private var strUserFullName: String? = null
        private var strUserEmail:String?=null
        private var strConfirmUserEmail:String?=null
        private var strAddress:String?=null
        private var strLatitude:String?=null
        private var strLongitude:String?=null

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    progressBar.visibility = View.INVISIBLE
                    if (!task.isSuccessful) {
                        textViewToken.text = task.exception?.message
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    firebaseid=token
                    //Toast.makeText(this@SignIn, firebaseid, Toast.LENGTH_LONG).show()
                    textViewMessage.text = "Your FCM Token is:"
                    textViewToken.text = token
                    Log.d("Token", token)
                })
        //creating notification channel if android version is greater than or equals to oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        //opening dashboard
        buttonOpenDashboard.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("http://bit.ly/2GEIPlu")
            startActivity(i)
        }

        //copying the token
        buttonCopyToken.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("token", textViewToken.text)
            clipboard.primaryClip = clip
            Toast.makeText(this@SignIn, "Copied", Toast.LENGTH_LONG).show()

        }
        val buttonPhoneAuth = findViewById<Button>(R.id.buttonPhoneAuth)
        buttonPhoneAuth.isEnabled = false

        val buttonPhoneAuthNext = findViewById<Button>(R.id.buttonPhoneAuthNext)
        val lytUserEmail = findViewById<View>(R.id.lytUserEmail)
        val lytTiesto = findViewById<View>(R.id.lytTiesto)
        val edtUserEmail = findViewById<EditText>(R.id.edtUserEmail)
        val edtConfirmUserEmail = findViewById<EditText>(R.id.edtConfirmUserEmail)
        val tvSTerms = findViewById<TextView>(R.id.tvSTerms)
        tvSTerms.setOnClickListener{
            val intent = Intent(this, TermsAndConditions::class.java)
            startActivity(intent)
        }
        val chkbUserPolicy = findViewById<CheckBox>(R.id.chkS1)
        chkbUserPolicy.setOnClickListener{
            buttonPhoneAuth.isEnabled = chkbUserPolicy.isChecked

            }
        lytUserEmail.visibility = View.GONE
        // Set button listen
        buttonPhoneAuth.setOnClickListener {
            strUserFullName = ""
            strUserEmail =""
            strConfirmUserEmail=""
            strUserFullName = edtUserFullName.text.toString().trim()
            strUserEmail = edtUserEmail.text.toString().trim()
            strConfirmUserEmail = edtConfirmUserEmail.text.toString().trim()
            if (!strUserFullName.equals("") && !strUserFullName.equals(null) && !strUserEmail.equals(
                    ""
                ) && !strUserEmail.equals(null) && strUserEmail.equals(strConfirmUserEmail)
            )
                doPhoneLogin()
            else
                Toast.makeText(baseContext, "Please check your email address!!", Toast.LENGTH_LONG).show()

        }
        buttonPhoneAuthNext.setOnClickListener {
            lytTiesto.visibility = View.GONE
            lytUserEmail.visibility = View.VISIBLE
        }


    }

    private fun doPhoneLogin() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.PhoneBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build()

        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val idpResponse = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                callMainActivity(user)

            } else {
                /**
                 * Sign in failed. If response is null the user canceled the
                 * sign-in flow using the back button. Otherwise check
                 * response.getError().getErrorCode() and handle the error.
                 */
                Toast.makeText(baseContext, "Error!! Phone Number Authentication Failed", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun callMainActivity(user: FirebaseUser?){
        val user = User(
            user!!.phoneNumber!!, firebaseid, strUserEmail,
            strUserFullName, strAddress, strLatitude, strLongitude
        )
        SharedPrefManager.getInstance(applicationContext).userLogin(user)
            uploadUserDetails()
    }
    private fun uploadUserDetails() {
        var isInternetPresent: Boolean? = null
        val UPLOAD_URL="https://www.instrov.com/malakane_init/user_upload.php"
        val cd = ConnectionDetector(applicationContext)
        isInternetPresent = cd.isConnectingToInternet
        if (isInternetPresent) {
            //Showing the progress dialog
            progressBar.visibility = View.VISIBLE
//            val loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false)
            val stringRequest = object : StringRequest(Request.Method.POST, UPLOAD_URL,
                    Response.Listener { s ->
                        //Disimissing the progress dialog
                        progressBar.visibility = View.INVISIBLE
                        if (s.equals("Success") ) {
                            Snackbar.make(lytTiesto, "Login Successful.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            finish()
                            val intent = Intent(this, LandingPage::class.java)
                            startActivity(intent)
                        } else {
                            Snackbar.make(lytTiesto, "Error!!"+s, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                        }
                    },
                    Response.ErrorListener { volleyError ->
                        //Dismissing the progress dialog
                        progressBar.visibility = View.INVISIBLE

                        //Showing toast
                        if (volleyError is TimeoutError || volleyError is NoConnectionError) {
                            Toast.makeText(applicationContext, applicationContext.getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show()
                        } else if (volleyError is AuthFailureError) {
                            //
                            Toast.makeText(applicationContext, volleyError.message.toString(), Toast.LENGTH_LONG).show()

                        } else if (volleyError is ServerError) {
                            //
                            Toast.makeText(applicationContext, volleyError.message.toString(), Toast.LENGTH_LONG).show()

                        } else if (volleyError is NetworkError) {
                            //
                            Toast.makeText(applicationContext, volleyError.message.toString(), Toast.LENGTH_LONG).show()

                        } else if (volleyError is ParseError) {
                            Toast.makeText(applicationContext, volleyError.message.toString(), Toast.LENGTH_LONG).show()

                        }
                    }) {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {

                    val user = SharedPrefManager.getInstance(applicationContext).user
                    val strUserFullName = user.user_fullname
                    val strUserPhonenumber = user.phonenumber
                    val strUserFirebaseId = user.firebaseid
                    val strUserEmailAddress = user.user_priviledge

                    //String strPriceRange= edtPriceRange.getText().toString().trim();
                    //Creating parameters
                    val params = Hashtable<String, String>()
                    //Adding parameters
                    params["struserfullname"] = strUserFullName
                    params["struserphonenumber"] = strUserPhonenumber
                    params["struseremail"] = strUserEmailAddress
                    params["struserfirebaseid"] = strUserFirebaseId
                    return params
                }
            }

            //Creating a Request Queue
            val requestQueue = Volley.newRequestQueue(this)
            val socketTimeout = 30000//30 seconds - change to what you want
            val policy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringRequest.retryPolicy = policy


            //Adding request to the queue
            requestQueue.add(stringRequest)
        } else {
            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            val snackbar = Snackbar
                    .make(lytUserEmail, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") { uploadUserDetails() }

            // Changing message text color
            snackbar.setActionTextColor(Color.RED)

            // Changing action button text color
            val sbView = snackbar.view
            val textView = sbView.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(Color.YELLOW)
            snackbar.show()
        }
    }
    override fun onBackPressed() {
        if (lytUserEmail.visibility == View.VISIBLE) {
            lytUserEmail.visibility = View.GONE
            lytTiesto.visibility = View.VISIBLE
        } else {
            finish()
        }
    }


}