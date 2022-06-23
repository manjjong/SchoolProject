package com.jm.schoolproject

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.jm.schoolproject.activity.FeedbackPageActivity
import com.jm.schoolproject.activity.LoginActivity
import com.jm.schoolproject.fragment.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var adapter : ViewpagerAdapter
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    companion object {
        lateinit var prefs : PreferenceUtil
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onPermission()
        prefs = PreferenceUtil(applicationContext)

        UserData.getInstance().login(callback = {
            if (UserData.getInstance().state == UserData.LOGOUT_STATE) {
                var intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                updateLoginMenu()
            }
        })
        viewPager = findViewById<ViewPager>(R.id.dlg_date_viewpager)
        tabLayout = findViewById<TabLayout>(R.id.dlg_date_tablayout)
        initAdapter()
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.workout)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.feedback)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.home)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.calendar)
        tabLayout.getTabAt(4)?.setIcon(R.drawable.flag)

        val toolbar: Toolbar = findViewById(R.id.toolbar) // toolBar를 통해 App Bar 생성
        setSupportActionBar(toolbar) // 툴바 적용

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        // 네비게이션 드로어 생
        drawerLayout = findViewById(R.id.drawer_layout)

        // 네비게이션 드로어 내에있는 화면의 이벤트를 처리하기 위해 생성
        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fbMenu -> {
                    val intent = Intent(this, FeedbackPageActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.login -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.logout -> {
                    UserData.getInstance().logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.notice -> {
                    //startActivity<FriendRequestList>()
                    true
                } else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateLoginMenu()
    }

    private fun updateLoginMenu() {
        if(navigationView == null) return
        if(UserData.getInstance().state == UserData.LOGIN_STATE) {
            navigationView.menu.getItem(2).isVisible = false
            navigationView.menu.getItem(3).isVisible = true
        }
        else {
            navigationView.menu.getItem(2).isVisible = true
            navigationView.menu.getItem(3).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // 클릭한 툴바 메뉴 아이템 id 마다 다르게 실행하도록 설정
        when(item.itemId){
            android.R.id.home->{
                // 햄버거 버튼 클릭시 네비게이션 드로어 열기
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdapter() {
        adapter = ViewpagerAdapter(supportFragmentManager)
        adapter.addFragment(ExerciseMenuFragment(), "WORKOUT")
        adapter.addFragment(FeedbackMenuFragment(), "FEEDBACK")
        adapter.addFragment(HomeMenuFragment(), "HOME")
        adapter.addFragment(CalendarMenuFragment(), "CALENDAR")
        adapter.addFragment(GoalMenuFragment(), "GOAL")
    }

    private fun onPermission() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 100) {
            if(grantResults.isNotEmpty()) {
                for(grant in grantResults) {
                    if(grant != PackageManager.PERMISSION_GRANTED) finish()
                }
            }
        }
    }
}