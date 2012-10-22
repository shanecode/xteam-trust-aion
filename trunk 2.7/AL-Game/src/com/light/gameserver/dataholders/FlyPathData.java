package com.light.gameserver.dataholders;

import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.light.gameserver.model.templates.flypath.FlyPathEntry;

/**
 * @author KID
 */
@XmlRootElement(name = "flypath_template")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlyPathData {
	@XmlElement(name = "flypath_location")
	private List<FlyPathEntry> list;

	private TIntObjectHashMap<FlyPathEntry> loctlistData = new TIntObjectHashMap<FlyPathEntry>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (FlyPathEntry loc : list) {
			loctlistData.put(loc.getId(), loc);
		}
	}

	public int size() {
		return loctlistData.size();
	}

	public FlyPathEntry getPathTemplate(int i) {
		return loctlistData.get(i);
	}
}
