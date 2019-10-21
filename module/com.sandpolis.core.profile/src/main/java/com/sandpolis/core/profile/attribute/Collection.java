package com.sandpolis.core.profile.attribute;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import com.sandpolis.core.instance.ProtoType;
import com.sandpolis.core.proto.pojo.Attribute.ProtoCollection;
import com.sandpolis.core.proto.util.Result.ErrorCode;

/**
 * An attribute collection is simply a set of {@link Document}s. They can
 * represent anything from the set of currently running processes or the disks
 * in a system.
 * 
 * @author cilki
 * @since 5.1.1
 */
@Entity
public class Collection implements ProtoType<ProtoCollection> {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int db_id;

	@Column
	private String id;

	@OneToMany(cascade = CascadeType.ALL)
	@MapKeyColumn
	private Map<String, Document> documents;

	public Document document(String id) {
		var document = documents.get(id);
		if (document == null) {
			document = new Document(id);
			documents.put(id, document);
		}
		return document;
	}

	public Collection(String id) {
		this.id = id;
		this.documents = new HashMap<>();
	}

	protected Collection() {
	}

	@Override
	public ErrorCode merge(ProtoCollection delta) throws Exception {
		// TODO
		return ErrorCode.OK;
	}

	@Override
	public ProtoCollection extract() {
		// TODO
		return null;
	}
}
