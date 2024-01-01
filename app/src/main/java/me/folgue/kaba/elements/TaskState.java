package me.folgue.kaba.elements;

/**
 * Represents the different states a task can be.
 */
public enum TaskState {
    Todo,
    InProgress,
    Done;

    public static TaskState ofString(String rawState) {
        return switch (rawState.toLowerCase()) {
            case "todo" -> TaskState.Todo;
            case "inprogress" -> TaskState.InProgress;
            case "done" -> TaskState.Done;
            default -> null;
        };
    }
}
