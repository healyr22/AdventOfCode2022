package com.robware.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Day7 {

    public static final int TOTAL_DISK_SPACE = 70000000;
    public static final int MIN_SPACE_TO_USE = 40000000;

    enum ContentType {
        DIR,
        FILE
    }

    static abstract class Content {

        private final String name;
        private final Directory parent;

        Content(final String name, final Directory parent) {
            this.name = name;
            this.parent = parent;
        }

        String getName() {
            return this.name;
        }

        Directory getParentDir() {
            return this.parent;
        }

        String getFullPathName() {
            String name = getName();
            Directory d = getParentDir();
            while(d != null) {
                name = d.getName() + "/" + name;
                d = d.getParentDir();
            }
            return name;
        }

        abstract Content getChildContent(String name);

        abstract int getSize();

        abstract List<Content> getContents();

        abstract ContentType getType();
    }

    static class Directory extends Content {

        private final List<Content> contents = new ArrayList<>();

        Directory(final String name, final Directory parent) {
            super(name, parent);
        }

        void addContent(Content content) {
            this.contents.add(content);
        }

        @Override
        Content getChildContent(String name) {
            for (Content content : this.contents) {
                if(content.getName().equals(name)) {
                    return content;
                }
            }
            return null;
        }

        @Override
        List<Content> getContents() {
            return this.contents;
        }

        @Override
        public int getSize() {
            return this.contents.stream().map(Content::getSize).reduce(0, Integer::sum);
        }

        @Override
        ContentType getType() {
            return ContentType.DIR;
        }

        @Override
        public String toString() {
            return getName() + " (dir)";
        }
    }

    static class File extends Content {

        private final int size;

        File(final String name, final Directory parent, final int size) {
            super(name, parent);
            this.size = size;
        }

        @Override
        public int getSize() {
            return this.size;
        }

        @Override
        Content getChildContent(String name) {
            throw new IllegalArgumentException("File " + getName() + " cannot contain child " + name);
        }

        @Override
        List<Content> getContents() {
            return List.of();
        }

        @Override
        ContentType getType() {
            return ContentType.FILE;
        }

        @Override
        public String toString() {
            return getName() + " (file, size=" + getSize() + ")";
        }
    }

    static Content findContent(Content content, String pathToContent) {
        final var pathSplit = Arrays.asList(pathToContent.split("/"));

        final var childContent = content.getChildContent(pathSplit.get(0));
        if(childContent == null || pathSplit.size() == 1) {
            return childContent;
        }

        return findContent(childContent,
                String.join("/", pathSplit.subList(1, pathSplit.size())));
    }

    static Directory getOrCreateDirectory(Directory currentDir, String dirSearchName) {
        if(currentDir.getChildContent(dirSearchName) == null) {
            // Doesn't exist yet - create
            currentDir.addContent(new Directory(dirSearchName, currentDir));
        }
        return (Directory) currentDir.getChildContent(dirSearchName);
    }

    static String prettyString(Content content) {
        return prettyString(content, 0, new StringBuilder());
    }

    static String prettyString(Content content, int tabCount, StringBuilder sb) {
        String tabs = "   ".repeat(tabCount);
        sb.append(tabs)
                .append("- ")
                .append(content.toString())
                .append("\n");

        for (Content childContent : content.getContents()) {
            prettyString(childContent, tabCount + 1, sb);
        }

        return sb.toString();
    }

    record DirCount(Directory dir, int count) implements Comparable<DirCount> {
        @Override
        public int compareTo(DirCount d) {
            return d.count - this.count;
        }

        @Override
        public String toString() {
            return "(" + dir.getName() + ", " + count + ")";
        }
    }

    public static void main(String[] args) {

        try (Scanner s = new Scanner(System.in)) {
            final Directory rootDir = new Directory("root", null);
            Directory currentDir = null;
            while(s.hasNextLine()) {
                final var line = s.nextLine();

                if(line.charAt(0) == '$') {
                    // Command
                    final var split = line.split(" ");
                    final var command = split[1];
                    if(command.equals("cd")) {
                        // Change Dir
                        final var newDirName = split[2];
                        if(newDirName.equals("/")) {
                            currentDir = rootDir;
                        } else if(newDirName.equals("..")) {
                            currentDir = currentDir.getParentDir();
                        } else {
                            currentDir = getOrCreateDirectory(currentDir, newDirName);
                        }
                    } else if(command.equals("ls")) {
                        // IGNORE??
                        continue;
                    } else {
                        throw new IllegalArgumentException("Unknown command: " + command);
                    }
                } else if(line.startsWith("dir")) {
                    // Directory
                    final var dirName = line.split(" ")[1];
                    getOrCreateDirectory(currentDir, dirName);
                } else {
                    // File
                    final var split = line.split(" ");
                    final var fileSize = Integer.parseInt(split[0]);
                    final var fileName = split[1];
                    currentDir.addContent(new File(fileName, currentDir, fileSize));
                }
            }
            System.out.println("Pretty print:");
            System.out.println(prettyString(rootDir));

            Set<DirCount> sizeSet = new TreeSet<>();
            fillMap(rootDir, sizeSet);

            System.out.println("Size set:" + sizeSet);

            final int rootSize = sizeSet.iterator().next().count;
            final var spaceNeeded = rootSize - MIN_SPACE_TO_USE;
            System.out.println("Total size we need to remove: " + spaceNeeded);

            final var it = sizeSet.iterator();
            DirCount previous = it.next(); // Skip root
            // Set is ordered from highest size to lowest. Keep iterating until you find one too
            // small and the answer is the one just beforehand (previous)
            while(it.hasNext()) {
                final var next = it.next();
                if(next.count < spaceNeeded) {
                    System.out.println("Answer is dir " + previous.dir.getFullPathName() + " with " +
                            "size " + previous.count);
                    return;
                }
                previous = next;
            }
        }
    }

    static void fillMap(Content content, Set<DirCount> sizeSet) {
        if(content.getType() == ContentType.FILE) {
            return;
        }

        sizeSet.add(new DirCount((Directory) content, content.getSize()));

        for (Content childContent : content.getContents()) {
            fillMap(childContent, sizeSet);
        }
    }
}
