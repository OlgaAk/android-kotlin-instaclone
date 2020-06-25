import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import com.example.android.instaclone.R

fun descriptionFormatted(username: String, description: String?, alt_description: String? = "no decsription"): Spanned {
    val descriptionFinal = description ?: alt_description
    val sb = StringBuilder()
    sb.apply {
        append("<b>")
        append(username)
        append("</b> ")
        append(descriptionFinal)
    }
    val max_characters = 110
    val string = when (sb.length){
        in 1..max_characters -> sb.toString()
        else -> "${sb.substring(0,max_characters+1).toString()}... "
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

}