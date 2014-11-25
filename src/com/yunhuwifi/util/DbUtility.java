package com.yunhuwifi.util;

import java.util.List;

import android.content.Context;
import net.tsz.afinal.FinalDb;

public class DbUtility {

	public static void save(Context context, Object object) {
		FinalDb db = FinalDb.create(context);
		db.save(object);
	}

	public static void delete(Context context, Object object) {
		FinalDb db = FinalDb.create(context);
		db.delete(object);
	}

	public static <T> List<T> findAll(Context context, Class<T> clazz) {
		FinalDb db = FinalDb.create(context);
		return db.findAll(clazz);
	}

	public static <T> T findById(Context context, Class<T> clazz, int id) {
		FinalDb db = FinalDb.create(context);
		return db.findById(id, clazz);
	}

	public static <T> T findByWhere(Context context, Class<T> clazz,
			String strWhere) {
		FinalDb db = FinalDb.create(context);
		List<T> list = db.findAllByWhere(clazz, strWhere);

		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public static <T> List<T> findAllByWhere(Context context, Class<T> clazz,
			String strWhere) {
		FinalDb db = FinalDb.create(context);
		return db.findAllByWhere(clazz, strWhere);
	}

	public static <T> List<T> findByWhere(Context context, Class<T> clazz,
			String strWhere, String order) {
		FinalDb db = FinalDb.create(context);
		return db.findAllByWhere(clazz, strWhere, order);
	}

	public static <T> List<T> findAll(Context context, Class<T> clazz,
			String order) {
		FinalDb db = FinalDb.create(context);
		return db.findAll(clazz, order);
	}

	public static <T> void deleteById(Context context, Class<T> clazz, int id) {
		FinalDb db = FinalDb.create(context);
		db.deleteById(clazz, id);
	}

	public static <T> void deleteAll(Context context, Class<T> clazz) {
		FinalDb db = FinalDb.create(context);
		db.deleteAll(clazz);

	}

	public static <T> void deleteByWhere(Context context, Class<T> clazz,
			String strWhere) {
		FinalDb db = FinalDb.create(context);
		db.deleteByWhere(clazz, strWhere);
	}

	public static void update(Context context, Object router) {
		FinalDb db = FinalDb.create(context);
		db.update(router);
	}

	public static <T> T find(Context context, Class<T> clazz) {
		FinalDb db = FinalDb.create(context);
		List<T> list = db.findAll(clazz);
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
}
