import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import com.example.app.ui.admin.AdminActivity
import com.example.app.ui.user.UserActivity
import androidx.core.content.ContextCompat
import com.example.app.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mengubah warna status bar menggunakan colorPrimaryDark
        val statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        // Logic for checking role can go here
        val userRole = "user" // For demonstration purpose
        when (userRole) {
            "admin" -> {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
            "user" -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
        }

        // Mengakses NavHostFragment menggunakan findFragmentById
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        // Pastikan navHostFragment tidak null dan Anda bisa mengakses navController
        navHostFragment?.let {
            val navController = it.navController
            // Anda bisa melakukan navigasi menggunakan navController di sini
        }
    }
}
