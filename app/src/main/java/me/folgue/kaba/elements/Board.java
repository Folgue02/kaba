package me.folgue.kaba.elements;

import java.util.*;

public class Board {
    public String id;
    public int lastGivenId = 0;
    public final TreeSet<Task> tasks;

    public Board(String id) {
        this.id = id;
        this.tasks = new TreeSet<>();
    }

    public void createNewTask(String name, String description, TaskState state) {
        this.tasks.add(new Task(this.lastGivenId++, name, description, state));
    }

    public void createNewTask(String name, String description) {
        this.createNewTask(name, description, TaskState.Todo);
    }

    public Optional<Task> getTask(int taskId) {
        return this.tasks.stream()
            .filter((t) -> t.id == taskId)
            .findFirst();
    }

    /**
     * Changes the state of a task.
     * @param taskId Id of the task.
     * @param newTaskState New state of the task.
     * @return {@code true} if the task existed and the state was changed,
     * {@code false} if the task didn't exist.
     */
    public boolean setTaskState(int taskId, TaskState newTaskState) {
        Optional<Task> wrappedTask = this.getTask(taskId);

        if (wrappedTask.isPresent()) {
            wrappedTask.get().state = newTaskState;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a task with the id specified.
     * @param targetId Id of the task to be removed.
     * @return {@code true} if the task is removed, {@code false} if the task doesn't
     * exist and wasn't removed.
     */
    public boolean removeTask(int targetId) {
        return this.tasks.removeIf((t) -> t.id == targetId);
    }
}
