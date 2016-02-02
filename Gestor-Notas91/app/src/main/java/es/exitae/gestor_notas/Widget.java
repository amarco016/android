package es.exitae.gestor_notas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        //Creamos dos intents, uno para el boton de añadir una nueva nota y otra para ver el mapa
        Intent intentMapa=new Intent(context,MapaActivity.class);
        Intent intentNota= new Intent(context,PantallaEdicionCreacion.class);
        PendingIntent pendingMapa=PendingIntent.getActivity(context,0,intentMapa,0);
        PendingIntent pendingNota=PendingIntent.getActivity(context,0,intentNota,0);
        RemoteViews rViews=new RemoteViews(context.getPackageName(),R.layout.widget);
        rViews.setOnClickPendingIntent(R.id.mapa,pendingMapa);
        rViews.setOnClickPendingIntent(R.id.sumar,pendingNota);
        appWidgetManager.updateAppWidget(appWidgetId,rViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

