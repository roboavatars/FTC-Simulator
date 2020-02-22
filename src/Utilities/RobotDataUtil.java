package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class RobotDataUtil {

    private String basePath = "src/Logs/";
    //private String basePath ="C:/Users/jonat/Downloads/motorola-moto_e__4_-192.168.49.1_7303/sdcard/FIRST/robotLogs/";
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;

    private ArrayList<Object[]> dataArray;
    private boolean logAccel;

    public RobotDataUtil(String logName, boolean logAccel) {
        basePath += logName + ".csv";
        this.logAccel = logAccel;
    }

    public void parseLogFile() {
        String curLine;
        int lineNum = 0;
        dataArray = new ArrayList<>();
        double prevXV = 0, prevYV = 0, prevThetaV = 0, prevTime = 0;

        try {
            File robotDataLog = new File(basePath);
            bufferedReader = new BufferedReader(new FileReader(robotDataLog));

            if (logAccel) {
                File accelLog = new File("src/Logs/Accel.csv");
                fileWriter = new FileWriter(accelLog);
                fileWriter.write("AccelX,AccelY,AccelTheta\n");
            }

            while ((curLine = bufferedReader.readLine()) != null) {
                if (lineNum != 0) {
                    String[] data = curLine.split(",");

                    double time = Double.parseDouble(data[1]);
                    double velocityX = Double.parseDouble(data[5]), velocityY = Double.parseDouble(data[6]), velocityTheta = Double.parseDouble(data[7]);
                    double accelX = (prevXV - velocityX) / ((prevTime - time) / 1000);
                    double accelY = (prevYV - velocityY) / ((prevTime - time) / 1000);
                    double accelTheta = (prevThetaV - velocityTheta) / ((prevTime - time) / 1000);

                    dataArray.add(new Object[]{time, Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]), velocityX, velocityY, velocityTheta, accelX, accelY, accelTheta, Boolean.parseBoolean(data[8]), Boolean.parseBoolean(data[9]), Boolean.parseBoolean(data[10]), Boolean.parseBoolean(data[11]), Boolean.parseBoolean(data[12]), Boolean.parseBoolean(data[13])});
                    //dataArray.add(new Object[]{Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]), Double.parseDouble(data[6]), Double.parseDouble(data[7]), Double.parseDouble(data[8]), Double.parseDouble(data[9]), Double.parseDouble(data[10]), Boolean.parseBoolean(data[11]), Boolean.parseBoolean(data[12]), Boolean.parseBoolean(data[13]), Boolean.parseBoolean(data[14]), Boolean.parseBoolean(data[15]), Boolean.parseBoolean(data[16])});

                    if (logAccel) {
                        fileWriter.write(accelX + "," + accelY + "," + accelTheta + "\n");
                        prevXV = velocityX; prevYV = velocityY; prevThetaV = velocityTheta; prevTime = time;
                    }
                }
                lineNum++;
            }
            bufferedReader.close();
            if (logAccel) fileWriter.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public Object[] getData(int index) {
        return dataArray.get(index);
    }

    public double getTimeDiff(int index) {
        if (index != 0) {
            Object[] prev = dataArray.get(index - 1);
            Object[] cur = dataArray.get(index);
            return (double) cur[0] - (double) prev[0];
        } else {
            return 0;
        }
    }

    public int getNumOfPoints() {return dataArray.size();}
}
