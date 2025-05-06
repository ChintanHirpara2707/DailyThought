package com.github.gouravkhunger.quotesapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.github.gouravkhunger.quotesapp.R
import com.github.gouravkhunger.quotesapp.databinding.ActivityQuotesBinding
import com.github.gouravkhunger.quotesapp.store.Preference
import com.github.gouravkhunger.quotesapp.ui.fragments.QuoteFragmentDirections
import com.github.gouravkhunger.quotesapp.viewmodels.QuoteViewModel
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.AppUpdaterUtils.UpdateListener
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesActivity : AppCompatActivity() {

    var atHome = true
    private lateinit var binding: ActivityQuotesBinding
    private val viewModel by viewModels<QuoteViewModel>()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myBookmarksImgBtn.setOnClickListener {
            val action = QuoteFragmentDirections
                .actionQuoteFragmentToBookmarkFragment()
            val navController = Navigation.findNavController(binding.quotesNavHostFragment)

            navController.navigate(action)
            it.visibility = View.GONE

            with(binding) {
                settingsBtn.visibility = View.GONE
                backToQuotePage.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.myBookMarks)
            }

            atHome = false
        }

        binding.backToQuotePage.setOnClickListener {
            super.onBackPressed()
            it.visibility = View.GONE

            with(binding) {
                settingsBtn.visibility = View.VISIBLE
                myBookmarksImgBtn.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.app_name)
            }

            atHome = true
        }

//        binding.settingsBtn.setOnClickListener {
//            SettingsFragment().show(supportFragmentManager, SettingsFragment.TAG)
//        }

        // Update theme once everything is set up
        setTheme(R.style.Theme_QuotesApp)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            viewModel.saveSetting(Preference.ASK_NOTIF_PERM, false)

            val message = if (isGranted) "Notifications set up successfully!"
                else  "Daily quotes won't work! Please set up notifications in settings."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        }

        Log.d("QuotesActivity", "onCreate: ${viewModel.getSetting(Preference.ASK_NOTIF_PERM)}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.getSetting(Preference.ASK_NOTIF_PERM)) {
            if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (!viewModel.getSetting(Preference.CHECK_FOR_UPDATES)) return

        val appUpdaterUtils = AppUpdaterUtils(this)
            .setUpdateFrom(UpdateFrom.GITHUB)
            .setGitHubUserAndRepo("gouravkhunger", "QuotesApp")
            .withListener(object : UpdateListener {
                override fun onSuccess(update: Update, isUpdateAvailable: Boolean) {
                    if (!isUpdateAvailable) return

                    Snackbar.make(
                        findViewById(R.id.flFragment),
                        getString(R.string.update_text, update.latestVersion),
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.download) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.download_url))
                            )
                        )
                    }.show()
                }

                override fun onFailed(error: AppUpdaterError) {
                    // ignore
                }
            })
        appUpdaterUtils.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!atHome) {
            with(binding) {
                settingsBtn.visibility = View.VISIBLE
                backToQuotePage.visibility = View.GONE
                myBookmarksImgBtn.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.app_name)
            }

            atHome = true
        }
    }
}
