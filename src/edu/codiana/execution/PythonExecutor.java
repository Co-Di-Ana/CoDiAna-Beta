package edu.codiana.execution;

import edu.codiana.core.iface.IProgExecutor;
import edu.codiana.execution.monitors.ProcessMonitor;
import edu.codiana.execution.results.ProgRunResult;
import edu.codiana.utils.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Jan Hybš
 * @version 1.0
 */
public class PythonExecutor implements IProgExecutor {

    private final String EMPTY = "";

    private String stdin = EMPTY, stdout = EMPTY, stderr = EMPTY;

    private File file;



    public PythonExecutor() {
    }



    @Override
    public ProgRunResult execute() throws IOException {
	return execute(3, 5 * 1000);
    }



    @Override
    public ProgRunResult execute(int numberOfTests, int timeLimit) throws IOException {

	ProgRunResult result = null;

	for (int i = 0; i < numberOfTests; i++) {
	    ProcessMonitor pm = new ProcessMonitor(
		    String.format("python %s", file.getPath()));
	    pm.setStdin(stdin);
	    pm.setStdout(stdout);
	    pm.setStderr(stderr);
	    pm.run(timeLimit);
	    ProgRunResult temp = new ProgRunResult(pm);

	    if (result == null || (temp.exit == 0 && result.time == 0 && temp.time < result.time))
		result = temp;

	}


	return result;
    }



    @Override
    public void setSourceFile(File file) throws FileNotFoundException {
	if (file == null || !file.exists())
	    throw new FileNotFoundException("File does not exists");

	this.file = file;
    }



    @Override
    public void setStdin(String stdin) {
	this.stdin = stdin;
    }



    @Override
    public void setStdout(String stdout) {
	this.stdout = stdout;
    }



    @Override
    public void setStderr(String stderr) {
	this.stderr = stderr;
    }



    @Override
    public void setPath(String sourcePath) {
	//# ignore
    }



    @Override
    public String getPath() {
	//# ignore
	return null;
    }



}
