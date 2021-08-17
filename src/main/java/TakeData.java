import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

class TakeData {
    static Queue<String> neededList = new ConcurrentLinkedQueue<String>();
    private int baseLength;

    void findAll(File dir, int depth, String mask) {
        setBaseLength(dir);
        Stack<File> stack = new Stack<File>();
        stack.push(dir);
        while (!stack.isEmpty()) {
            File child = stack.pop();
            if (child.isDirectory()) {
                if ((checkLength(child, baseLength) < depth))
                    for (File f : child.listFiles()) stack.push(f);
            } else if (child.isFile()) {
                if (child.toString().contains(mask)) {
                    neededList.add(returnOnlyName(child));
                }
            }
        }
    }

    private void setBaseLength(File directory) {
        String[] array = directory.getPath().split(Pattern.quote(File.separator));
        baseLength = array.length;
    }

    private int checkLength(File child, int base) {
        String[] array = child.getPath().split(Pattern.quote(File.separator));
        return array.length - base;
    }

    private String returnOnlyName(File child) {
        String[] array = child.getPath().split(Pattern.quote(File.separator));
        return array[array.length - 1];
    }
}

