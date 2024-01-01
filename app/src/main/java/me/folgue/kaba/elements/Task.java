package me.folgue.kaba.elements;

public class Task implements Comparable<Task> {
    public int id;
    public String name;
    public String description;
    public TaskState state;

    
    Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description; 
        this.state = TaskState.Todo;
    }

    Task(int id, String name, String description, TaskState state) {
        this(id, name, description);
        this.state = state;
    }

    public int compareTo(Task other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.format("\n\tID: %d\n\tName: %s\n\tDescription: %s, \n\tState: %s", this.id, this.name, this.description, this.state);
    }
}