package br.com.battlebits.ycommon.common.permissions.groups;

import java.util.Arrays;
import java.util.List;

public class OwnerGroup extends GroupInterface {

	@Override
	public List<String> getPermissions() {
		return Arrays.asList("*");
	}
}
