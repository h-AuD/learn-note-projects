package AuD.template.project.test;

/**
 * Description: 排序算法
 *
 * @author AuD/胡钊
 * @ClassName SortAlgorithm
 * @date 2022/1/4 17:41
 * @Version 1.0
 */
public class SortAlgorithm {

    /**
     * 冒泡排序
     * @param arrays
     * @return
     */
    public int[] bubbleSort(int[] arrays){
        if (arrays.length == 0)
            return arrays;
        // 核心思想便是双重for循环
        for (int a = 0; a < arrays.length; a++) {    // TODO 每次循环的结果就是将最大的元素放置在最后一个
            for (int i = a+1; i < arrays.length; i++) {
                // 判断当前元素是否大于下一个元素?互换位置:nothing
                if(arrays[a]>arrays[i]){
                    // 通过临时变量(tmp)来完成数据交换,这个临时变量是否可以省略?
                    /*
                    int tmp = arrays[i];
                    arrays[i] = arrays[a];
                    arrays[a]=tmp;
                    */
                    arrays[a] = arrays[a]+arrays[i];
                    arrays[i] = arrays[a]-arrays[i];    // i == a(old)
                    arrays[a] = arrays[a]-arrays[i];    // a == i(old)
                }
            }
        }
        return arrays;
    }

    /**
     * 选择排序
     * @param arrays
     * @return
     */
    public static int[] selectionSort(int[] arrays) {
        if (arrays.length == 0)
            return arrays;
        for (int i = 0; i < arrays.length; i++) {
            int minIndex = i;
            for (int j = i; j < arrays.length; j++) {
                if (arrays[j] < arrays[minIndex]) //找到最小的数
                    minIndex = j; //将最小数的索引保存
            }
            int temp = arrays[minIndex];
            arrays[minIndex] = arrays[i];
            arrays[i] = temp;
        }
        return arrays;
    }

    /**
     * 插入排序
     * @param arrays
     * @return
     */
    public static int[] insertionSort(int[] arrays) {
        if (arrays.length == 0)
            return arrays;
        int current;
        for (int i = 0; i < arrays.length - 1; i++) {
            current = arrays[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < arrays[preIndex]) {
                arrays[preIndex + 1] = arrays[preIndex];
                preIndex--;
            }
            arrays[preIndex + 1] = current;
        }
        return arrays;
    }

}
