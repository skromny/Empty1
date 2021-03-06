package com.recrutify.empty1.empty1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.R.attr.gravity
import android.R.attr.overridesImplicitlyEnabledSubtype
import android.graphics.PixelFormat
import android.graphics.Color
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.*
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.recrutify.empty1.empty1.candidate.CandidateActivity

class IncomingCallReceiver : BroadcastReceiver() {

    var isMoving: Boolean = false

    companion object {
        var rgcView: View? = null
        var candidate: CandidateContainer? = null
    }


    private var _xDelta: Int = 0
    private var _yDelta: Int = 0

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.getResources()
                .getDisplayMetrics()
                .density
        return Math.round(dp.toFloat() * density) as Int
    }

    override fun onReceive(context: Context, intent: Intent) {

        try
        {
            Log.i("RGC.START", "-------------------")

            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE); //RINGING/OFFHOOK/IDLE

            if(state == "RINGING")
            {
                val msisdn = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER); //RINGING/OFFHOOK/IDLE

                if(msisdn == null)
                    return

                val msisdn2 = if (msisdn.startsWith("+")) msisdn.substring(3) else msisdn

                Log.i("RGC.MSISDN", msisdn)
                Log.i("RGC.MSISDN2", msisdn2)

                "/utils/checkNumber/${msisdn2}".httpGet()
                        .responseObject(CandidateContainer.Deserializer()) { request, response, result ->

                            Log.i("RESULT", result.toString())
                            Log.i("REQUEST", request.toString())

                            result.success { s->
                                Log.d("SUCCESS", s.toString())

                                candidate = s


                                //===========

                                val type = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                                else
                                    WindowManager.LayoutParams.TYPE_PHONE

                                val params = WindowManager.LayoutParams(
                                        WindowManager.LayoutParams.MATCH_PARENT,
                                        dpToPx(context, 120), type,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                                        //or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                                        PixelFormat.TRANSLUCENT)

                                params.gravity = Gravity.NO_GRAVITY

                                val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                rgcView = inflater.inflate(R.layout.overlay_call_view, null)

                                val avatarImage = rgcView?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.avatarImage)

                                Glide.with(context)
                                        .load(s.candidate.avatarLink)
                                        .listener(object : RequestListener<Drawable> {
                                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                                avatarImage?.setImageResource(R.drawable.ic_boss_1)
                                                return true
                                            }

                                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                                return false
                                            }
                                        })
                                        .into(avatarImage)


                                val candidateName = rgcView?.findViewById<TextView>(R.id.candidateName)
                                candidateName?.text = s.candidate.name

                                candidateName?.setOnClickListener({

                                    Log.i("TAG", "candidateName ->   CliCK on: ${candidate?.candidate?.name}" )

                                    val intent = Intent(context.applicationContext, CandidateActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)


                                    if(rgcView != null) {
                                        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                        wm.removeView(rgcView)
                                        rgcView = null;
                                    }

                                })

                                val projectName = rgcView?.findViewById<TextView>(R.id.projectName)

                                if(s.projects?.size > 0)
                                {
                                    projectName?.text =  s.projects[0].name + ", " + s.projects[0].companyName

                                    projectName?.setOnClickListener({
                                        Log.i("TAG", "projectName ->   CliCK on: ${s.projects[0].name}")
                                    })

                                    val projectLocation = rgcView?.findViewById<TextView>(R.id.projectLocation)
                                    projectLocation?.text = s.projects[0].location

                                }



                                val closeButton = rgcView?.findViewById<Button>(R.id.btnClose)

                                closeButton?.setOnClickListener({
                                    if(rgcView != null) {
                                        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                        wm.removeView(rgcView)
                                        rgcView = null;
                                    }


                                })

                                rgcView?.setOnTouchListener(object : View.OnTouchListener {
                                    override fun onTouch(v: View?, event: MotionEvent): Boolean {

                                        val X = event.rawX.toInt()
                                        val Y = event.rawY.toInt()
                                        when (event.action and MotionEvent.ACTION_MASK) {
                                            MotionEvent.ACTION_DOWN -> {
                                                Log.i("ACTION_DOWN", "!")
                                                val lParams = v?.layoutParams as WindowManager.LayoutParams
                                                //_xDelta = X - lParams.
                                                _yDelta = Y - lParams.y
                                            }
                                            MotionEvent.ACTION_UP -> {
                                                Log.i("ACTION_UP", "!")
                                            }
                                            MotionEvent.ACTION_POINTER_DOWN -> {
                                                Log.i("ACTION_POINTER_DOWN", "!")
                                            }
                                            MotionEvent.ACTION_POINTER_UP -> {
                                                Log.i("ACTION_POINTER_UP", "!")
                                            }
                                            MotionEvent.ACTION_MOVE -> {
                                                Log.i("ACTION_MOVE", "!")
                                                val layoutParams = v?.layoutParams as WindowManager.LayoutParams
                                                layoutParams.y = Y - _yDelta
                                                v.setLayoutParams(layoutParams)

                                                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                                wm.updateViewLayout(v, layoutParams)
                                            }

                                        }
                                        return true;
                                    }
                                })

                                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                wm.addView(rgcView, params)




                                //===========

                            }
                            result.failure { e ->
                                Log.e("FAILURE", e.toString())
                            }

                        }


            }
            else if(state == "IDLE")
            {
                if(rgcView != null){
                    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    wm.removeView(rgcView)
                }
                rgcView = null;
            }

        }
        catch (ex: Exception)
        {
            Log.e("RGC", ex.toString())
        }




    }
}
