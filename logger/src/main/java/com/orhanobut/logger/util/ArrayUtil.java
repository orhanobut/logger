package com.orhanobut.logger.util;

import android.util.Pair;

import java.util.Arrays;

/**
 * Created by pengwei08 on 2015/7/25.
 * Thanks to zhutiantao for providing an array of analytical methods.
 */
public final class ArrayUtil {

    /**
     * 获取数组的纬度
     */
    public static int getArrayDimension(Object objects) {
        int dim = 0;
        for (int i = 0; i < objects.toString().length(); ++i) {
            if (objects.toString().charAt(i) == '[') {
                ++dim;
            } else {
                break;
            }
        }
        return dim;
    }

    public static Pair<Pair<Integer, Integer>, String> arrayToObject(Object object) {
        StringBuilder builder = new StringBuilder();
        int cross, vertical;
        if (object instanceof int[][]) {
            int[][] ints = (int[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (int[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof byte[][]) {
            byte[][] ints = (byte[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (byte[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof short[][]) {
            short[][] ints = (short[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (short[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof long[][]) {
            long[][] ints = (long[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (long[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof float[][]) {
            float[][] ints = (float[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (float[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof double[][]) {
            double[][] ints = (double[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (double[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof boolean[][]) {
            boolean[][] ints = (boolean[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (boolean[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else if (object instanceof char[][]) {
            char[][] ints = (char[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (char[] ints1 : ints) {
                builder.append(arrayToString(ints1).second).append("\n");
            }
        } else {
            Object[][] objects = (Object[][]) object;
            cross = objects.length;
            vertical = cross == 0 ? 0 : objects[0].length;
            for (Object[] objects1 : objects) {
                builder.append(arrayToString(objects1).second).append("\n");
            }
        }
        return Pair.create(Pair.create(cross, vertical), builder.toString());
    }

    /**
     * 数组转化为字符串
     */
    public static Pair arrayToString(Object object) {
        StringBuilder builder = new StringBuilder("[");
        int length = 0;
        if (object instanceof int[]) {
            int[] ints = (int[]) object;
            length = ints.length;
            for (int i : ints) {
                builder.append(i).append(",\t");
            }
        } else if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            length = bytes.length;
            for (byte item : bytes) {
                builder.append(item).append(",\t");
            }
        } else if (object instanceof short[]) {
            short[] shorts = (short[]) object;
            length = shorts.length;
            for (short item : shorts) {
                builder.append(item).append(",\t");
            }
        } else if (object instanceof long[]) {
            long[] longs = (long[]) object;
            length = longs.length;
            for (long item : longs) {
                builder.append(item).append(",\t");
            }
        } else if (object instanceof float[]) {
            float[] floats = (float[]) object;
            length = floats.length;
            for (float item : floats) {
                builder.append(item).append(",\t");
            }
        } else if (object instanceof double[]) {
            double[] doubles = (double[]) object;
            length = doubles.length;
            for (double item : doubles) {
                builder.append(item).append(",\t");
            }
        } else if (object instanceof boolean[]) {
            boolean[] booleans = (boolean[]) object;
            length = booleans.length;
            for (boolean item : booleans) {
                builder.append(item).append(",\t");
            }
        } else if (object instanceof char[]) {
            char[] chars = (char[]) object;
            length = chars.length;
            for (char item : chars) {
                builder.append(item).append(",\t");
            }
        } else {
            Object[] objects = (Object[]) object;
            length = objects.length;
            for (Object item : objects) {
                builder.append(SystemUtil.objectToString(item)).append(",\t");
            }
        }
        return Pair.create(length, builder.replace(builder.length() - 2, builder.length(), "]").toString());
    }

    /**
     * 是否为数组
     */
    public static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    /**
     * 获取数组类型
     *
     * @param object 如L为int型
     */
    public static char getType(Object object) {
        if (isArray(object)) {
            String str = object.toString();
            return str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("[") + 2).charAt(0);
        }
        return 0;
    }

    /**
     * 遍历数组
     */
    private static void traverseArray(StringBuilder result, Object object) {
        if (!isArray(object)) {
            result.append(object.toString());
            return;
        }
        if (getArrayDimension(object) == 1) {
            switch (getType(object)) {
                case 'I':
                    result.append(Arrays.toString((int[]) object)).append("\n");
                    return;
                case 'D':
                    result.append(Arrays.toString((double[]) object)).append("\n");
                    return;
                case 'Z':
                    result.append(Arrays.toString((boolean[]) object)).append("\n");
                    return;
                case 'B':
                    result.append(Arrays.toString((byte[]) object)).append("\n");
                    return;
                case 'S':
                    result.append(Arrays.toString((short[]) object)).append("\n");
                    return;
                case 'J':
                    result.append(Arrays.toString((long[]) object)).append("\n");
                    return;
                case 'F':
                    result.append(Arrays.toString((float[]) object)).append("\n");
                    return;
                case 'L':
                    result.append(Arrays.toString((Object[]) object)).append("\n");
                default:
                    return;
            }
        }
        for (int i = 0; i < ((Object[]) object).length; i++) {
            traverseArray(result, ((Object[]) object)[i]);
        }
    }

    public static String traverseArray(Object object) {
        StringBuilder result = new StringBuilder();
        traverseArray(result, object);
        return result.toString();
    }

}
