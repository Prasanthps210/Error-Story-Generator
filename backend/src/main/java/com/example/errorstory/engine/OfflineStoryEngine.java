package com.example.errorstory.engine;

import org.springframework.stereotype.Component;

@Component
public class OfflineStoryEngine {

    public OfflineResult generate(String difficulty, ParsedError parsed) {

        String exception = parsed.getExceptionType();
        int line = parsed.getLineNumber();
        String file = parsed.getClassName();

        // ---------- NullPointerException ----------
        if (exception.equalsIgnoreCase("NullPointerException")) {

            String title = "The Ghost of Null Reference";

            String story =
                    "In the kingdom of " + file + ", everything was alive and working.\n" +
                            "Suddenly, a ghost named Null appeared.\n" +
                            "The program tried to talk to it, but it had no form.\n" +
                            "The JVM screamed in confusion.\n" +
                            "The execution stopped in fear.\n" +
                            "The developer realized someone forgot to create an object.\n" +
                            "The ghost vanished when initialization was fixed.";

            String fix =
                    "FIX: A NullPointerException happens when you use an object that was never created.\n" +
                            "Always initialize your objects before calling methods.\n" +
                            "Add null checks before accessing properties.\n" +
                            "Check object creation near line " + line + ".";

            String example = "";

            if ("INTERMEDIATE".equalsIgnoreCase(difficulty)) {
                example =
                        "// ❌ Bad Code\n" +
                                "User user = null;\n" +
                                "user.getName();\n\n" +
                                "// ✅ Good Code\n" +
                                "User user = new User();\n" +
                                "if (user != null) {\n" +
                                "    user.getName();\n" +
                                "}";
            }

            return new OfflineResult(title, story, fix, example);
        }

        // ---------- ArrayIndexOutOfBoundsException ----------
        if (exception.equalsIgnoreCase("ArrayIndexOutOfBoundsException")) {

            String title = "The Array Boundary Breaker";

            String story =
                    "In " + file + ", an array guarded its borders carefully.\n" +
                            "A reckless index crossed the forbidden boundary.\n" +
                            "The JVM blew the whistle immediately.\n" +
                            "Execution crashed with panic.\n" +
                            "The developer checked the loop conditions.\n" +
                            "One wrong index caused chaos.\n" +
                            "The boundary was restored with proper validation.";

            String fix =
                    "FIX: This happens when you access an index outside array size.\n" +
                            "Always check array length before accessing elements.\n" +
                            "Indexes must be between 0 and array.length - 1.\n" +
                            "Inspect loops near line " + line + ".";

            String example = "";

            if ("INTERMEDIATE".equalsIgnoreCase(difficulty)) {
                example =
                        "// ❌ Bad Code\n" +
                                "int[] arr = {1,2,3};\n" +
                                "System.out.println(arr[5]);\n\n" +
                                "// ✅ Good Code\n" +
                                "int[] arr = {1,2,3};\n" +
                                "if (index < arr.length) {\n" +
                                "    System.out.println(arr[index]);\n" +
                                "}";
            }

            return new OfflineResult(title, story, fix, example);
        }

        // ---------- ArithmeticException ----------
        if (exception.equalsIgnoreCase("ArithmeticException")) {

            String title = "The Division Disaster";

            String story =
                    "In the land of " + file + ", numbers worked peacefully.\n" +
                            "Suddenly, someone tried dividing by zero.\n" +
                            "Math itself refused to cooperate.\n" +
                            "The JVM stopped everything immediately.\n" +
                            "The logs showed an illegal operation.\n" +
                            "The developer facepalmed.\n" +
                            "Validation saved the day.";

            String fix =
                    "FIX: Never divide by zero.\n" +
                            "Always check divisor before division.\n" +
                            "Add input validation.\n" +
                            "Look at calculations near line " + line + ".";

            String example = "";

            if ("INTERMEDIATE".equalsIgnoreCase(difficulty)) {
                example =
                        "// ❌ Bad Code\n" +
                                "int result = a / b;\n\n" +
                                "// ✅ Good Code\n" +
                                "if (b != 0) {\n" +
                                "    int result = a / b;\n" +
                                "} else {\n" +
                                "    System.out.println(\"Division by zero avoided\");\n" +
                                "}";
            }

            return new OfflineResult(title, story, fix, example);
        }

        // ---------- Default: Not supported ----------
        return new OfflineResult(
                "Offline Story Not Available",
                "This error type (" + exception + ") is not yet supported in offline mode.\n" +
                        "Please enable AI mode for detailed story generation.",
                "FIX: No offline fix template available for this exception type.",
                ""
        );
    }
}
