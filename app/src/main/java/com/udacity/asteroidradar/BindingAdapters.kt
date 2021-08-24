package com.udacity.asteroidradar


import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter
import java.net.URL

//Define adapter for image of the day
@BindingAdapter("daily_image")
fun setDailyImage(imageView: ImageView, url: String?) {
    Picasso.with(imageView.context).load(url).into(imageView)
}

//Define adapter to set image description
@BindingAdapter("daily_image_description")
fun setDescriptionOfDailyImage(imageView: ImageView, title: String?) {
    val context = imageView.context
    if (title != "") {
        imageView.contentDescription = title
    } else {
        imageView.contentDescription =
            String.format(context.getString(R.string.nasa_picture_of_day_content_description_format))

    }
}

//Define the adapater to set the list of Asteroids to the recylerview
@BindingAdapter("listData")
fun bindRecycler(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

//Set content description binding adapter to dynamically set teh description based on
//whether asteroid id hazardous or not
@BindingAdapter("setContentText")
fun setContent(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.contentDescription =
            String.format(context.getString(R.string.potentially_hazardous_asteroid_image))
    } else {
        imageView.contentDescription =
            String.format(context.getString(R.string.not_hazardous_asteroid_image))
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
