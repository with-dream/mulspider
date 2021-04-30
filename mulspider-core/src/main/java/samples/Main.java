package samples;

import com.example.core.context.Context;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Context.instance().prepare(new String[]{"wallpaper"});
        Context.instance().init();
        Context.instance().start();

        Scanner input = new Scanner(System.in);
        String str = input.next();
        System.out.println("==>finish");
    }
}
