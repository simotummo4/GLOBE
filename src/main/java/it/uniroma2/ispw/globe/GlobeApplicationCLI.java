package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.clicontroller.LogInCLIController;

public class GlobeApplicationCLI {

    static void run() {

        System.out.println("*********************************");
        System.out.println("*      GLOBE  APPLICATION       *");
        System.out.println("*********************************");
        System.out.println();

        new LogInCLIController().start();
    }

    public static void main(String[] args) {
        throw new IllegalStateException("Starter class");
    }
}