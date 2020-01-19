import java.util.Arrays;

public class BruteForce {
    public static long brute_force(int width, int height) {
        boolean[][] field = new boolean[width][height - 1];
        return enumerate(field, width, height - 1, 0, 0);
    }

    public static boolean hasFloatingBlocks(boolean[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 1; j < arr[i].length; j++) {
                if (arr[i][j] && !arr[i][j - 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int maxHeight(boolean[][] arr, int curr_x) {
        for (int j = arr[curr_x].length - 1; j >= 0; j--) {
            if (arr[curr_x][j]) {
                return j + 1;
            }
        }
        return 0;
    }

    public static int numberOfBlocks(boolean[][] arr) {
        int counter = 1;
        for (int j = 0; j < arr[0].length; j++) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i][j] && (i == 0 || !arr[i - 1][j])) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    public static boolean isValidCastle(boolean[][] arr) {
        //Check for floating
        if (hasFloatingBlocks(arr)) {
            return false;
        }

        //Check max height
        boolean has_max_height = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][arr[i].length - 1]) {
                has_max_height = true;
                break;
            }
        }
        if (!has_max_height) {
            return false;
        }
        //Check blocks
        return numberOfBlocks(arr) % 2 == 0;
    }

    public static long enumerate(boolean[][] arr, int width, int height, int curr_x, int curr_y) {
        if (curr_y >= height) {
            return enumerate(arr, width, height, curr_x + 1, 0);
        }
        if (curr_x == width - 1) {
            //System.out.println(Arrays.deepToString(arr));
            int highest_achieved_height = 0;
            for (int i = 0; i < width - 1; i++) {
                highest_achieved_height = Math.max(highest_achieved_height, maxHeight(arr, i));
                if (highest_achieved_height == height) {
                    break;
                }
            }
            int last_row_height = curr_x == 0 ? 0 : maxHeight(arr, curr_x - 1);
            int current_blocks = numberOfBlocks(arr);
            if (highest_achieved_height == height) {
                if (current_blocks % 2 == 0) {
                    long temp = last_row_height + 1 + (height - last_row_height) / 2;
                    //System.out.println("Returning: " + temp);
                    return temp;
                } else {
                    long temp = (height - last_row_height + 1) / 2;
                    //System.out.println("Returning: " + temp);
                    return temp;
                }
            } else {
                long temp = (current_blocks + (height - last_row_height)) % 2 == 0 ? 1 : 0;
                //System.out.println("Returning: " + temp);
                return temp;
            }

        }
        for (int i = curr_y; i < height; i++) {
            arr[curr_x][i] = false;
        }
        long counter = enumerate(arr, width, height, curr_x + 1, 0);
        arr[curr_x][curr_y] = true;
        return counter + enumerate(arr, width, height, curr_x, curr_y + 1);
    }
}
