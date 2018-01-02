package com.recrutify.empty1.empty1

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest
import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Build
import android.R.attr.targetSdkVersion
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.CursorLoader
import android.provider.Settings
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_login.*
import java.net.URL
import java.util.ArrayList
import javax.xml.transform.Result
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: MainActivity.UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                if(checkSelfPermission("android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_PHONE_STATE), 0);
                }
                if(checkSelfPermission("android.permission.INTERNET") != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(arrayOf(android.Manifest.permission.INTERNET), 0);
                }
                if(!Settings.canDrawOverlays(this))
                {
                    //requestPermissions(arrayOf(Settings.ACTION_MANAGE_OVERLAY_PERMISSION), 0);
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + packageName))
                    startActivityForResult(intent, 1234)

                }
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                PermissionChecker.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == PermissionChecker.PERMISSION_GRANTED
            }
        }

        // Set up the login form.
        //populateAutoComplete()
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return
//        }
//
//        loaderManager.initLoader(0, null, this)
//    }

//    private fun mayRequestContacts(): Boolean {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true
//        }
//        if (checkSelfPermission(permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true
//        }
//        if (shouldShowRequestPermissionRationale(permission.READ_CONTACTS)) {
//            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok,
//                            { requestPermissions(arrayOf(permission.READ_CONTACTS), MainActivity.REQUEST_READ_CONTACTS) })
//        } else {
//            requestPermissions(arrayOf(permission.READ_CONTACTS), MainActivity.REQUEST_READ_CONTACTS)
//        }
//        return false
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
//                                            grantResults: IntArray) {
//        if (requestCode == MainActivity.REQUEST_READ_CONTACTS) {
//            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete()
//            }
//        }
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserLoginTask(emailStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

//    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
//        return CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE),
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
//    }

//    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
//        val emails = ArrayList<String>()
//        cursor.moveToFirst()
//        while (!cursor.isAfterLast) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS))
//            cursor.moveToNext()
//        }
//
//        addEmailsToAutoComplete(emails)
//    }

//    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {
//
//    }

//    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        val adapter = ArrayAdapter(this@MainActivity,
//                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)
//
//        email.setAdapter(adapter)
//    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        val ADDRESS = 0
        val IS_PRIMARY = 1
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        private val SHA_1 = "SHA-1"
        private val DIGITS_LOWER = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        private val DIGITS_UPPER = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        fun encodeHex(data: ByteArray, toLowerCase: Boolean = true): CharArray {
            return encodeHex(data, if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
        }

        fun encodeHex(data: ByteArray, toDigits: CharArray): CharArray {
            val l = data.size
            val out = CharArray(l shl 1)
            var i = 0
            var j = 0
            while (i < l) {
                out[j++] = toDigits[(240 and data[i].toInt()).ushr(4)]
                out[j++] = toDigits[15 and data[i].toInt()]
                i++
            }
            return out
        }

        fun getDigest(algorithm: String): MessageDigest {
            try {
                return MessageDigest.getInstance(algorithm)
            } catch (e: NoSuchAlgorithmException) {
                throw IllegalArgumentException(e)
            }

        }

        fun sha1Bytes(data: ByteArray): ByteArray {
            return getDigest(SHA_1).digest(data)
        }

        fun sha1(data: ByteArray): String {
            return String(encodeHex(sha1Bytes(data)))
        }

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                //http://rcapi.recrutify.io/utils/checkNumber/{telNumber}

                // Simulate network access.
                //Thread.sleep(2000)
                //val result = URL("http://rcapi.recrutify.io/utils/checkNumber/{telNumber}"). .readText()
                //"http://rcapi.recrutify.io/utils/checkNumber/{telNumber}".httpPost().body("{user: \"\"}").response { request, response, result -> { response.headers }  }

                FuelManager.instance.apply {
                    basePath = "http://rcapi.recrutify.io"
                    baseHeaders = mutableMapOf("Content-Type" to "application/json")
                }


                //7efe0a853268d6b3b080005b87c17058bbcb665b
                val sha1Password = sha1("anowak".toByteArray())
                val body = "{\"login\":\"artur.nowak@ascendious.com\",\"password\":\"$sha1Password\"}"

                "/auth/login"
                        .httpPost()
                        .body(body)
                        .responseObject(LoginResponse.Deserializer()) { request, response, result ->

                            result.success { o ->
                                Log.i("Object", o.firstName)

                                FuelManager.instance.apply {
                                    basePath = "http://rcapi.recrutify.io"
                                    baseHeaders = FuelManager.instance.baseHeaders?.plus(Pair("Authorization", o.token))
                                }

                                //FuelManager.instance.baseHeaders?.plus(Pair("Authorization", o.token))
                                val intent = Intent(applicationContext, Main2Activity::class.java)
                                startActivity(intent)
                            }

                        }



            } catch (e: InterruptedException) {
                Log.e("EXCEPTION", e.toString())
                return false
            }

            return DUMMY_CREDENTIALS
                    .map { it.split(":") }
                    .firstOrNull { it[0] == mEmail }
                    ?.let {
                        // Account exists, return true if the password matches.
                        it[1] == mPassword
                    }
                    ?: true
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                //finish()
            } else {
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }
}
