package aris.kots.adminclientapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
public class TabPagerAdapter extends FragmentStatePagerAdapter {
	String isAdmin;
	int TabNumber;
    public TabPagerAdapter(FragmentManager fm,String isAdmin) {
    super(fm);
    this.isAdmin = isAdmin;
    if(isAdmin.equals("yes"))
    	TabNumber = 4;
    else
    	TabNumber = 2;
  }
    
  @Override
  public Fragment getItem(int i) {
    switch (i) {
        case 0:
            //Fragement for First Tab
            return new ArisTabCustom();
        case 1:
           //Fragment for Second
            return new DeviceInterfacesTab();
        case 2:
            //Fragment for Admin 1
            return new InsertMaliciousAdminTab();
        case 3:
            //Fragment for Admin 2
            return new DeleteUserAdminTab();
        }
    return null;
  }
  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return TabNumber; //No of Tabs
  }
    }