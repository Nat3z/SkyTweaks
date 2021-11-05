package com.natia.secretmod.extensions;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtensionList {
    private static List<Extension> extensionList = new ArrayList<>();
    public static void addExtension(Extension extension) {
        extensionList.add(extension);
    }

    public static List<Extension> getExtensionList() {
        return new ArrayList<>(extensionList);
    }
}
