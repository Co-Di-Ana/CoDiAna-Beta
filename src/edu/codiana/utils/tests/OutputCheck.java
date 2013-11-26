package edu.codiana.utils.tests;

import edu.codiana.utils.Strings;
import edu.codiana.utils.io.File;
import java.io.*;

/**
 * @author Hans
 */
public class OutputCheck extends SimpleCheck {
    private static final int NO_TOLERANCE = 0;

    private float linesOriginal = -1;
    private float linesUser = -1;
    private float linesError = -1;
    private float linesOk = -1;
    private boolean strictMode;
    private long valueCheck;



    /**
     * returns similarity of the given files. Both files are compared line-by-line so result is <br
     * />
     * <code>1 - e / max(t0, t1)</code> where: <br />
     * <b>e</b> is error (if lines aren't the same or one of them exists )<br />
     * <b>t0</b> and <b>t1</b> are numbers representing total line count of the given files
     *
     * @param originalFile
     * @param userFile
     * @return number from 0.0 to 1.0 (optimal) where 0 represents no similarity at all and 1.0
     * represents identical files
     * @throws Exception when files are missing
     */
    public void calculateResult (File originalFile, File userFile) throws FileNotFoundException, IOException {

        if (isTeacher) {
            setResults (ZERO_VALUE, NO_VALUE, NO_VALUE);
            return;
        }

        if (valueCheck == NO_VALUE) {
            setResults (NO_VALUE, NO_VALUE, NO_VALUE);
            return;
        }


        linesOk = linesError = linesOriginal = linesUser = 0;
        boolean originalEnd = false;
        boolean userEnd = false;

        BufferedReader originalBR = new BufferedReader (new FileReader (
                originalFile));
        BufferedReader userBR = new BufferedReader (new FileReader (userFile));

        String oline, uline;
        oline = uline = "";

        while (true) {

            //# checking original
            if (!originalEnd) {
                do {
                    oline = originalBR.readLine ();
                    if (oline == null) {
                        originalEnd = true;
                        break;
                    } else {
                        //# ignore empty lines
                        if (Strings.isOnlyWhitespace (oline))
                            continue;
                        linesOriginal++;
                        break;
                    }
                } while (true);
            }

            //# checking copy
            if (!userEnd) {
                do {
                    uline = userBR.readLine ();
                    if (uline == null) {
                        userEnd = true;
                        break;
                    } else {
                        //# ignore empty lines
                        if (Strings.isOnlyWhitespace (uline))
                            continue;
                        linesUser++;
                        break;
                    }
                } while (true);
            }

            //# both of them finished?
            if (originalEnd && userEnd)
                break;

            //# couting errors and ok's
            if (oline == null || uline == null)
                linesError++;
            else if (!uline.equals (oline))
                linesError++;
            else
                linesOk++;
        }
        float value = aproximateResult (linesOk, (linesOriginal + linesUser) / 2, NO_TOLERANCE);
        value = strictMode ? (int) value : value;
        setResults (linesOk, value, strictMode ? ZERO_VALUE : value * weight);
    }



    /**
     * Method sets value of output check
     *
     * @param value (-1, 0, 1)
     */
    public void setValueCheck (long value) {
        valueCheck = value;
        strictMode = value == 1;
    }



    @Override
    public boolean isSupported () {
        return true;
    }



    private static float aproximateResult (float linesOK, float lines, float tolerance) {
        float diff = Math.abs (linesOK - lines);
        if (diff <= tolerance)
            return 1.0f;

        return linesOK / lines;
    }
}
