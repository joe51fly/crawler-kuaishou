import com.joe.kuaishou.KsApplication;
import com.joe.kuaishou.bean.Myfavorite;
import com.joe.kuaishou.mapper.MyfavoriteMapper;
import com.joe.kuaishou.service.MyfavoriteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@RunWith(SpringRunner.class)
////加载主启动类
//@SpringBootTest(classes = KsApplication.class)
public class TestMybatis {
    @Autowired
    private MyfavoriteService myfavoriteService;
//    @Test
//    public void test(){
//        //根据id查询
//        //KsLiveMyfavorite ksLiveMyfavorite = ksLiveMyfavoriteService.getKsLiveMyfavorite(1);
//
//        //查询全部
//        List<KsLiveMyfavorite> ksLiveMyfavoriteList = ksLiveMyfavoriteService.getAll();
//        Iterator<KsLiveMyfavorite> iterator = ksLiveMyfavoriteList.iterator();
//        while (iterator.hasNext()){
//            KsLiveMyfavorite ksLiveMyfavorite = iterator.next();
//            System.out.println(ksLiveMyfavorite);
//        }
//    }
//    @Test
//    public void test2(){
//        KsLiveMyfavorite ksLiveMyfavorite =new KsLiveMyfavorite();
//        ksLiveMyfavorite.setKsAnchorName("bbb");
//        ksLiveMyfavorite.setKsAnchorId("bbb");
//        ksLiveMyfavorite.setKsAnchorHeaderUrl("bbb");
//        boolean b = ksLiveMyfavoriteService.insertKsLiveMyfavorite(ksLiveMyfavorite);
//        if (b){
//            System.out.println("插入数据成功");
//        }
//    }
//    @Test
//    public void test3(){
//        KsLiveMyfavorite ksLiveMyfavorite =new KsLiveMyfavorite();
//        ksLiveMyfavorite.setId(2);
//        ksLiveMyfavorite.setKsAnchorName("ccc");
//        boolean b = ksLiveMyfavoriteService.updateKsLiveMyfavoriteByid(ksLiveMyfavorite);
//        if (b){
//            System.out.println("更新数据成功");
//        }
//    }
//    @Test
//    public void test4(){
//        List<KsLiveMyfavorite> list = new ArrayList<KsLiveMyfavorite>();
//        for (int i=0; i<5; i++){
//            KsLiveMyfavorite ksLiveMyfavorite =new KsLiveMyfavorite();
//            ksLiveMyfavorite.setKsAnchorName("fff"+i);
//            ksLiveMyfavorite.setKsAnchorId("fff"+i);
//            ksLiveMyfavorite.setKsAnchorHeaderUrl("fff"+i);
//            ksLiveMyfavorite.setDescription("fff"+i);
//            list.add(ksLiveMyfavorite);
//        }
//        int i = ksLiveMyfavoriteService.insertKsLiveMyfavoriteByList(list);
//        if (i > 0){
//            System.out.println("成功插入数据："+i + "条");
//        }
//    }
//    @Test
//    public void test5(){
//        boolean i = ksLiveMyfavoriteService.deleteMyfavoriteByid(21);
//        if (i){
//            System.out.println("删除数据成功");
//        }
//    }
}
