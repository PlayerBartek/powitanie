package com.example.powitanie;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My_channel_id";
    String imie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotification();

        EditText inputjakis = findViewById(R.id.editTextImie);
        Button pszycisk = findViewById(R.id.buttonPowitanie);
        pszycisk.setOnClickListener(v -> {
            if(inputjakis.getText().toString().matches("")){
                showNoNameDialog();
            }else{
                imie = inputjakis.getText().toString();
                showIsNameDialog();
            }
        });

    }

    private void showNoNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Błąd");
        builder.setMessage("Proszę wpisać swoje imię!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //nic bo no co innego?
            }
        });

        builder.create().show();
    }

    private void showIsNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potwierdzenie");
        builder.setMessage("Dynamicznie wygenerowana, np. \"Cześć "+imie+"! Czy chcesz otrzymać powiadomienie powitalne?");

        builder.setPositiveButton("Tak, poproszę", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendNotification();
            }
        });
        builder.setNegativeButton("Nie, dziękuję", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Rozumiem. Nie wysyłam powiadomienia.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    private void createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "kanał powiadomień";
            String description = "opis kanału powiadomień";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notifiactionManager = getSystemService(NotificationManager.class);
            notifiactionManager.createNotificationChannel(channel);
        }
    }
    private void sendNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.test48)
                .setContentTitle("Witaj!")
                .setContentText("Miło Cię widzieć, "+imie+"!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}