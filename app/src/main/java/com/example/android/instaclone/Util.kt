import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import com.example.android.instaclone.R

fun descriptionFormatted(username: String, description: String?, alt_description: String? = "no decsription"): String? {
    return description ?: alt_description


}