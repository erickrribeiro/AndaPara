package com.promobile.epilesia;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.promobile.epilesia.HMM.Viterbi;
import com.promobile.epilesia.sax.SymbolicAggregateApproXimation;

import java.util.Stack;

public class SensorService extends Service implements SensorEventListener {

    public static String TAG = "SensorService";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Stack arrayAmostragem;

    SymbolicAggregateApproXimation sax;
    private Viterbi viterbi;

    boolean mostra = true;
    @Override
    public void onCreate() {
        arrayAmostragem = new Stack<Double>();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        String [] estados    = {"1", "2"};
        //String[] observacoes = {"6","6","5","2","5","4","8","3","7","3","6","3","5","4","5","8","5","8","9","9","9","10","6","7","4","5","3","4","4","5  2","4","45","2","5","4","8","3","7","3","6","3","5","4","5  8","5","8","9","9","9","10","6","7","4","5","3","4","4","5  2","4","4"};
        //String[] observacoes = {"10","6","5","5","5","5","5","6","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","6","5","5","5","6","6","6","5","5","5"};
        String[] observacoes = {"10","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","5","6","5","5","5","5","5","5","5","6","6","5","5"};
        double[] probabilidadeInicial = {1.0, 0.0};


        double[][] transicaoProbabilidade = {{0.9697,    0.0303},
                { 0.0270,    0.9730},

        };
        double[][] simbolosProbabilidade  = {{ 0.0135, 0.0471, 0.1279, 0.1785, 0.1448, 0.1717, 0.1448, 0.0842, 0.0707, 0.0168},
                { 0.0034, 0.0135, 0.0909, 0.1953, 0.4512, 0.1212, 0.0337, 0.0202, 0.0135, 0.0572},

        };
        viterbi = new Viterbi(estados, observacoes, probabilidadeInicial, transicaoProbabilidade, simbolosProbabilidade);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    public void onStart(Intent intent, int startid) {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Toast.makeText(this, "Monitoramento ligado.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double moduloVetor = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));

        //double[] serieLocal = { 15.4256, 4.6771, 6.9505, 8.6540, 9.2024, 7.1259, 14.4321, 6.5939, 3.2372, 5.7250, 2.6338, 7.0923, 1.4493, 4.0168, 7.3107, 3.7164, 11.4739, 9.2652, 6.7233, 9.8489, 12.1492, 3.0187, 9.4455, 7.0089, 5.4856, 8.9545, 17.6969, 1.8774, 6.8354, 6.9225, 5.8013, 10.6076, 17.1834, 2.7819, 8.1526, 9.4285, 5.1026, 8.3191, 13.0653, 8.4906, 2.0104, 7.6872, 4.2934, 9.6349, 6.7641, 6.5465, 7.7033, 11.8043, 10.1968, 7.1990, 8.0946, 9.0879, 8.5728, 13.4417, 15.6769, 7.1016, 7.4246, 20.9920, 11.2561, 9.4210, 11.1570, 13.3820, 13.3739, 13.9705, 16.2773, 8.2686, 10.4160, 3.9976, 1.3868, 8.6665, 15.6807, 4.6689, 5.9594, 8.3212, 2.6688, 13.4293, 10.8989, 5.9993, 3.4181, 5.4434, 5.6591, 3.1237, 7.6436, 13.5399, 2.2581, 3.6971, 6.4794, 4.9859, 14.2981, 10.4152, 5.8687, 3.3822, 5.3036, 7.1257, 1.4462, 6.4612, 13.1071, 4.5748, 1.6260};
        double[] serieLocal = new double[99];
        if(arrayAmostragem.size() >= 99){

            for(int index=0; index<arrayAmostragem.size(); index++){
                serieLocal[index] = (double) arrayAmostragem.get(index);
            }

            try {
                sax = new SymbolicAggregateApproXimation(3, 10, serieLocal);
                sax.executar();
                String alfabeto = "";

                /*for (int letra: sax.getSax()){
                    alfabeto +=letra;
                }

                Log.d(TAG, String.format("SAX: %s", alfabeto));*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            arrayAmostragem.pop();

            int[] arraySax = sax.getSax();
            String[] observaçoes = new String[arraySax.length];

            for(int index=0; index< sax.getSax().length; index++){
                observaçoes[index] = ""+(arraySax[index]);
            }
            Log.d("Atividade", "Resultado:"+viterbi.forwardViterbi(observaçoes));
            this.mostra = false;
        }

        arrayAmostragem.add(0, moduloVetor);

        if(mostra) {
            Log.d(TAG, String.format("Modulo: %s", moduloVetor));
            Log.d(TAG, String.format("Tamanho da amostra: %s", arrayAmostragem.size()));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
